# Redis Cluster 部署速查

以下示例使用 3 个主节点，并手动完成节点清理、互相发现和 16384 个哈希槽的分配。

Compose 使用官方 `redis:latest` 镜像。该镜像不识别 Redis Stack 的 `REDIS_ARGS`，因此 Redis 参数通过 `command: >-` 传入。命令必须以 `redis-server` 开头，以便官方入口脚本降权到 `redis` 用户；不要改成 `sh -c`。

## 1. 替换环境变量

在每台主机上修改 `env/.env`：

```dotenv
REDIS_PASSWORD=所有节点使用相同密码
REDIS_PORT=6379
REDIS_CLUSTER_BUS_PORT=16379
CLUSTER_ANNOUNCE_IP=当前主机可被其他节点访问的IP
```

每个节点的 `CLUSTER_ANNOUNCE_IP` 不同，其余配置保持一致。确保节点之间可以访问 TCP 端口 `6379` 和 `16379`。

## 2. 部署节点

在每台主机的当前目录执行：

```sh
docker compose --env-file env/.env -f docker-compose.yaml config --quiet
docker compose --env-file env/.env -f docker-compose.yaml up -d
docker compose --env-file env/.env -f docker-compose.yaml ps
```

容器状态应为 `healthy`。如果修改过环境变量，需要重新创建容器：

```sh
docker compose --env-file env/.env -f docker-compose.yaml up -d --force-recreate
```

## 3. 设置节点变量

在任意安装了 `redis-cli` 且能访问所有节点的主机上执行：

```sh
export NODE_1_IP='192.168.1.11'
export NODE_2_IP='192.168.1.12'
export NODE_3_IP='192.168.1.13'
export REDIS_PORT='6379'
export REDIS_CLUSTER_BUS_PORT='16379'
export REDISCLI_AUTH='实际Redis密码'
```

将示例 IP 和密码替换成实际值。

## 4. 清理旧数据和集群状态

> **危险：** `FLUSHALL` 会永久删除节点中的全部数据，`CLUSTER RESET HARD` 会清除集群拓扑、槽位配置并重新生成节点 ID。只能在确认数据可以删除时执行。

依次清理 3 个节点：

```sh
for node in "$NODE_1_IP" "$NODE_2_IP" "$NODE_3_IP"; do
  redis-cli -h "$node" -p "$REDIS_PORT" FLUSHALL
  redis-cli -h "$node" -p "$REDIS_PORT" CLUSTER RESET HARD
done
```

确认每条命令均返回：

```text
OK
```

## 5. 使用 CLUSTER MEET 连接节点

让节点 1 发现另外两个节点：

```sh
redis-cli -h "$NODE_1_IP" -p "$REDIS_PORT" \
  CLUSTER MEET "$NODE_2_IP" "$REDIS_PORT" "$REDIS_CLUSTER_BUS_PORT"

redis-cli -h "$NODE_1_IP" -p "$REDIS_PORT" \
  CLUSTER MEET "$NODE_3_IP" "$REDIS_PORT" "$REDIS_CLUSTER_BUS_PORT"
```

查看节点关系：

```sh
redis-cli -h "$NODE_1_IP" -p "$REDIS_PORT" CLUSTER NODES
```

确认输出中已经出现 3 个节点，再继续分配哈希槽。

## 6. 创建分片并分配哈希槽

Redis Cluster 一共有 `16384` 个哈希槽。将它们平均分配给 3 个主节点：

```sh
redis-cli -h "$NODE_1_IP" -p "$REDIS_PORT" CLUSTER ADDSLOTSRANGE 0 5460
redis-cli -h "$NODE_2_IP" -p "$REDIS_PORT" CLUSTER ADDSLOTSRANGE 5461 10922
redis-cli -h "$NODE_3_IP" -p "$REDIS_PORT" CLUSTER ADDSLOTSRANGE 10923 16383
```

槽位分配如下：

| 节点   | 哈希槽范围    |
| ------ | ------------- |
| 节点 1 | `0-5460`      |
| 节点 2 | `5461-10922`  |
| 节点 3 | `10923-16383` |

## 7. 验证集群

检查集群状态：

```sh
redis-cli -h "$NODE_1_IP" -p "$REDIS_PORT" CLUSTER INFO
redis-cli -h "$NODE_1_IP" -p "$REDIS_PORT" CLUSTER NODES
```

正常结果应包含：

```text
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
```

使用 `-c` 开启集群重定向并测试跨节点读写：

```sh
redis-cli -c -h "$NODE_1_IP" -p "$REDIS_PORT" SET cluster:test 'ok'
redis-cli -c -h "$NODE_2_IP" -p "$REDIS_PORT" GET cluster:test
```

完成后清除当前 shell 中的认证信息：

```sh
unset REDISCLI_AUTH
```

## 注意事项

- `CLUSTER MEET` 只负责让节点互相发现，不会自动分配哈希槽。
- `CLUSTER ADDSLOTSRANGE` 必须在实际持有对应槽位的节点上执行。
- 所有 16384 个槽位分配完成后，集群状态才会变成 `ok`。
- 当前 Compose 使用 bridge 网络和端口映射，`CLUSTER_ANNOUNCE_IP` 必须是其他节点能够访问的宿主机 IP。
- 当前 Compose 未挂载 `/data` 持久化卷，重新创建容器会丢失 `nodes.conf` 和 Redis 数据；正式使用前应增加持久化卷。
