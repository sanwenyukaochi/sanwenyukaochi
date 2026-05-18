# 技术博客整理 Skill

## 适用场景

当用户提供以下任意内容时，使用本 Skill：

- 微信公众号文章
- 技术文档
- 官方文档片段
- 自己的开发笔记
- 代码示例
- 问题排查记录
- 架构设计讨论
- Java / Spring Boot / DDD / Docker / Redis / RabbitMQ 等技术素材

目标是将原始内容整理成一篇适合发布到 AstroPaper、个人博客或技术站点的 `.md` 技术文章。

---

## 核心目标

不要照搬原文。

应该将素材整理成用户自己的技术博客风格：

- 从真实问题出发
- 先讲背景，再讲概念
- 先说为什么，再说是什么
- 先给最小示例，再拆核心用法
- 加入项目实践视角
- 删除推广、广告、二维码、关注引导、推荐阅读等无关内容
- 保留技术核心
- 适当重写标题和描述
- 输出可直接保存为 `.md` 文件的最终版本

---

## 输出格式

最终输出必须是完整 Markdown 文件内容，包含 AstroPaper FrontMatter。

格式如下：

```md
---
title: 文章标题
author: 三文鱼烤翅
pubDatetime: 2026-05-18T00:00:00+08:00
slug: article-slug
featured: false
draft: false
tags:
  - Java
  - Spring Boot
description: "一句话说明这篇文章解决什么问题。"
---

正文内容
```

---

## FrontMatter 规则

### title

标题不要照搬营销标题。

应该改成清晰、工程化、技术博客风格的标题。

不推荐：

```text
别再瞎拼 URL 了！Spring Boot 这套 URI 工具太香
```

推荐：

```text
Spring Boot 中优雅构建 URI：别再手动拼接 URL
```

标题风格：

```text
技术名 + 解决的问题
技术名 + 实践方式
问题场景 + 推荐方案
```

示例：

```text
Spring Boot 中统一处理异常的实践方案
RabbitMQ 消费者 Ack、Reject、Nack 应该怎么选
基于 Outbox Pattern 实现可靠事件发布
Docker Compose 中如何优雅管理服务健康检查
```

### author

固定使用：

```yaml
author: 三文鱼烤翅
```

### pubDatetime

使用文章原发布时间。

如果没有明确时间，使用当前时间。

格式：

```yaml
pubDatetime: 2026-05-18T00:00:00+08:00
```

### slug

使用英文小写短横线命名。

示例：

```yaml
slug: spring-boot-uri-components-builder
slug: rabbitmq-ack-reject-nack
slug: spring-security-exception-handling
```

### tags

根据文章内容生成 2 到 6 个标签。

常用标签示例(仅示例，具体根据文章进行总结标签)：

```yaml
tags:
  - Java
  - Spring Boot
  - Spring MVC
  - Spring Security
  - RabbitMQ
  - Redis
  - Docker
  - DDD
  - PostgreSQL
  - Gradle
```

### description

用一句话说明文章解决什么问题。

不要写广告语。

推荐格式：

```text
整理某个技术在实际项目中的常见用法，说明它解决的问题、核心使用方式以及项目落地建议。
```

---

## 正文结构

正文必须按以下顺序整理。

```text
1. 背景
2. 问题
3. 推荐方案
4. 基本用法
5. 核心用法
6. 项目实践
7. 不推荐写法和推荐写法
8. 实际项目中怎么选
9. 总结
```

---

## 1. 背景

开头不要直接介绍 API。

应该先写真实开发中为什么会遇到这个问题。

示例：

````md
在 Spring Boot 开发中，我们经常需要构建 URL。

很多时候，代码里会直接使用字符串拼接：

```java
String url = "https://" + bucket + ".cos." + region + ".myqcloud.com" + output.getPath();
```

这种写法看起来简单，但在真实项目里很容易出现路径拼接、参数编码和特殊字符处理等问题。

所以这篇文章整理一下 Spring Boot 中更推荐的 URI 构建方式。
````

---

## 2. 问题

具体说明旧写法或常见写法有什么问题。

格式：

```md
## 手动拼接 URL 的问题

手动拼接 URL 主要有几个问题：

- 路径分隔符 `/` 容易混乱
- 查询参数需要自己处理编码
- 中文、空格、`+`、`&` 等特殊字符容易出错
- host、path、query 参数耦合在一起
- 后期维护和重构成本高
```

要求：

- 问题必须具体
- 不要空泛
- 最好结合代码说明

---

## 3. 推荐方案

介绍文章的核心技术或方案。

格式：

```md
## 推荐使用 `UriComponentsBuilder`

Spring 提供了 `UriComponentsBuilder`，可以用来构建 URI、处理路径变量、查询参数和编码问题。

它相比字符串拼接的优势是：

- 结构更清晰
- 参数处理更安全
- 编码行为更规范
- 代码更容易维护
```

这一节回答：

```text
这个东西是什么？
它解决什么问题？
为什么比原来的方式更好？
```

---

## 4. 基本用法

必须提供一个最小可运行或最小可理解示例。

格式：

````md
## 基本用法

```java
String url = UriComponentsBuilder.newInstance()
    .scheme("https")
    .host("example.com")
    .path("/search")
    .queryParam("q", "Spring Boot")
    .encode()
    .build()
    .toUriString();
```

生成结果：

```text
https://example.com/search?q=Spring%20Boot
```

这段代码里，`.scheme()` 用来设置协议，`.host()` 用来设置域名，`.path()` 用来设置路径，`.queryParam()` 用来设置查询参数。
````

要求：

- 代码要干净
- 不要保留公众号中混乱的压缩代码
- 变量命名要符合 Java 风格
- 示例后必须有解释

---

## 5. 核心用法

将原文中的多个功能点拆成多个小节。

格式：

````md
## 核心用法

### 构建普通 URL

说明这个场景适合什么时候用。

```java
代码示例
```

解释关键点。

### 添加查询参数

说明这个场景适合什么时候用。

```java
代码示例
```

解释关键点。

### 处理编码问题

说明这个场景适合什么时候用。

```java
代码示例
```

解释关键点。
````

要求：

- 每个小节只讲一个点
- 标题要清晰
- 代码块不要太长
- 删除无意义截图
- 如果截图只是运行结果，改成文本输出

---

## 6. 项目实践

必须加入“项目中怎么落地”。

这是用户博客区别于搬运文章的关键部分。

格式：

````md
## 项目中怎么落地

在真实项目中，不建议把 URL 构建逻辑散落在 Controller 或 Service 中。

如果是第三方 API，可以封装到独立的 Client 中。

如果是对象存储 URL，可以封装到专门的 URL Factory 中。

例如：

```java
@Component
@RequiredArgsConstructor
public class CosUrlFactory {

    public String buildObjectUrl(String bucket, String region, String path) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(bucket + ".cos." + region + ".myqcloud.com")
            .path(path)
            .encode()
            .build()
            .toUriString();
    }
}
```

这样业务代码只需要关注对象路径，而不需要关心 URL 拼接细节。
````

要求：

- 必须站在工程实践角度写
- 尽量联系 Spring Boot 项目
- 尽量给出封装建议
- 不要只停留在 API 使用层面

---

## 7. 不推荐写法和推荐写法

每篇技术博客都尽量加这一节。

格式：

````md
## 不推荐写法和推荐写法

不推荐：

```java
String url = "https://" + bucket + ".cos." + region + ".myqcloud.com" + output.getPath();
```

推荐：

```java
String url = UriComponentsBuilder.newInstance()
    .scheme("https")
    .host(bucket + ".cos." + region + ".myqcloud.com")
    .path(output.getPath())
    .encode()
    .build()
    .toUriString();
```

推荐写法的好处是：

- host 和 path 分离
- 特殊字符可以统一编码
- URL 结构更清晰
- 后续扩展 query 参数更方便
````

要求：

- 先展示旧写法
- 再展示推荐写法
- 最后说明为什么推荐

---

## 8. 实际项目中怎么选

如果文章里出现多个类、多个方案、多个工具，必须加选择表格。

格式：

```md
## 实际项目中怎么选

| 场景 | 推荐方式 |

|---|---|
| 普通 URL 构建 | `UriComponentsBuilder` |
| 带查询参数 | `.queryParam()` |
| 统一客户端基础 URL | `DefaultUriBuilderFactory` |
| 基于当前请求生成链接 | `ServletUriComponentsBuilder` |
| 基于 Controller 方法生成链接 | `MvcUriComponentsBuilder` |
```

要求：

- 表格要简洁
- 不要为了凑表格写无意义内容
- 场景必须来自真实开发

---

## 9. 总结

总结要写成可记忆规则。

格式：

```md
## 总结

在 Spring Boot 项目中，不建议直接使用字符串拼接 URL。

更推荐使用 Spring 提供的 URI 构建工具完成路径构建、参数拼接和编码处理。

简单记住：

- 拼路径：用 `.path()`
- 拼查询参数：用 `.queryParam()`
- 处理变量：用 URI 模板
- 统一客户端配置：用 `DefaultUriBuilderFactory`
- 基于当前请求：用 `ServletUriComponentsBuilder`

URL 构建看起来是小问题，但它经常出现在文件访问、第三方 API、回调地址、分页链接和重定向场景里。

把它从字符串拼接改成标准 URI 构建工具，代码会更安全，也更容易维护。
```

要求：

- 不要空话
- 不要鸡汤
- 用具体规则收尾
- 最后一段点明工程价值

---

## 清理规则

从公众号或网页文章整理时，必须删除以下内容：

- 公众号名称重复信息
- 原创声明
- 阅读原文
- 点赞、收藏、转发、在看
- 二维码图片
- 关注公众号引导
- 小程序引导
- 课程广告
- 电子书广告
- 推荐阅读列表
- 无意义封面图
- 与正文无关的图片
- 微信环境提示
- 评论区、留言区内容
- “本文完”“感谢阅读”等公众号套话

保留以下内容：

- 技术主题
- 核心概念
- 核心代码
- 运行结果
- 有价值的图表
- 有解释意义的截图
- 可以转成文字说明的内容

---

## 重写规则

整理文章时必须重写，而不是简单复制。

### 标题重写

营销标题改成工程标题。

示例：

```text
别再瞎拼 URL 了！Spring Boot 这套 URI 工具太香
```

改成：

```text
Spring Boot 中优雅构建 URI：别再手动拼接 URL
```

### 代码重写

公众号里压缩在一行的代码必须格式化。

错误示例：

```java
DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://www.pack.com") ;factory.setEncodingMode(EncodingMode.TEMPLATE_AND_VALUES);RestTemplate restTemplate = new RestTemplate();
```

整理后：

```java
DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://www.pack.com");
factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);
RestTemplate restTemplate = new RestTemplate();
```

### 表达重写

公众号表达通常偏宣传，需要改成技术说明。

不推荐：

```text
这套工具太香了，开发效率提升 10 倍。
```

推荐：

```text
这种方式可以减少手动拼接 URL 带来的编码问题，也能让 URL 结构更清晰。
```

---

## 文章风格

文章风格应符合用户习惯：

- 中文技术博客
- 工程实践导向
- 简洁但不省略关键点
- 偏 Java / Spring Boot 后端开发视角
- 多用“为什么这么做”
- 多用“不推荐 / 推荐”
- 多用“项目中怎么落地”
- 少用营销语
- 少用夸张表达
- 不要像官方文档翻译
- 不要像公众号软文

---

## 代码风格

Java 代码要求：

- 使用 4 空格缩进
- 链式调用换行
- 方法名清晰
- 变量命名语义明确
- 不使用无意义变量名
- 不要保留中文分号、奇怪空格
- import 非必要不写
- 示例代码尽量短

推荐链式调用格式：

```java
String url = UriComponentsBuilder.newInstance()
    .scheme("https")
    .host("example.com")
    .path("/search")
    .queryParam("q", keyword)
    .encode()
    .build()
    .toUriString();
```

---

## Markdown 规则

- 一级标题通常不需要写，因为 frontmatter 里已经有 title
- 正文从自然段或 `## 背景` 开始
- 每个大段使用 `##`
- 子场景使用 `###`
- 代码必须带语言标识
- 表格前后保留空行
- 列表不要过长
- 删除多余空行
- 中文和英文、代码之间适当留空格

示例：

````md
## 基本用法

```java
String url = UriComponentsBuilder.newInstance()
    .scheme("https")
    .host("example.com")
    .path("/search")
    .build()
    .toUriString();
```

这段代码会生成一个基础 URL。
````

---

## 最终输出要求

当用户要求“整理成博客”时，最终只输出完整 Markdown 文件内容。

不要额外解释太多。

如果用户要求“放到画布”，则创建一个 canvas 文档，内容为最终 `.md` 文章。

如果用户要求“给我最终版本代码”，则输出完整 Markdown 源码。

如果用户要求“只给 frontmatter”，则只输出 frontmatter。

---

## 固定文章模板

````md
---
title:
author: 三文鱼烤翅
pubDatetime:
slug:
featured: false
draft: false
tags:
  -
description: ""
---

## 背景

这里写实际开发中为什么会遇到这个问题。

## 问题

这里写旧写法或常见写法的问题。

## 推荐方案

这里写推荐使用的技术、工具或设计方式。

## 基本用法

```java
// 最小示例
```

这里解释代码。

## 核心用法

### 场景一

```java
// 示例代码
```

这里解释场景一。

### 场景二

```java
// 示例代码
```

这里解释场景二。

### 场景三

```java
// 示例代码
```

这里解释场景三。

## 项目中怎么落地

这里写真实项目中的封装方式、放在哪一层、如何避免代码散落。

```java
// 项目实践代码
```

## 不推荐写法和推荐写法

不推荐：

```java
// 不推荐写法
```

推荐：

```java
// 推荐写法
```

推荐写法的好处是：

- 好处一
- 好处二
- 好处三

## 实际项目中怎么选

| 场景 | 推荐方式 |

|---|---|
| 场景一 | 方式一 |
| 场景二 | 方式二 |
| 场景三 | 方式三 |

## 总结

这里用几条规则总结全文。
````

---

## 使用方式

用户之后可以这样下指令：

```text
使用技术博客整理 Skill，把下面这篇文章整理成 AstroPaper 博客。
```

或者：

```text
按照我的博客 Skill 整理这篇公众号文章，输出最终 md。
```

或者：

```text
用博客整理 Skill，删除公众号广告内容，保留技术核心，整理成我自己的文章风格。
```

---

## 核心原则

从问题出发，而不是从 API 出发。

整理成自己的工程实践文章，而不是搬运公众号。

