# syntax=docker/dockerfile:1

ARG RABBITMQ_TAG
FROM rabbitmq:${RABBITMQ_TAG}

ARG DELAYED_PLUGIN_URL

USER root

# 官方镜像是 Debian 系，装 curl 用于下载插件
RUN apt-get update \
  && apt-get install -y --no-install-recommends ca-certificates curl \
  && rm -rf /var/lib/apt/lists/*

# 下载 delayed 插件 .ez 到 RabbitMQ 插件目录
RUN set -eux; \
  echo "Downloading delayed plugin from: ${DELAYED_PLUGIN_URL}"; \
  curl -fsSL "${DELAYED_PLUGIN_URL}" -o "/plugins/rabbitmq_delayed_message_exchange.ez"; \
  chown rabbitmq:rabbitmq /plugins/rabbitmq_delayed_message_exchange.ez; \
  rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange

USER rabbitmq