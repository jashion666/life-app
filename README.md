# Love-Life 小程序服务端
## 模块说明
+ app-parent：模块的父pom，系统配置。
+ app-miniprogram： 小程序服务端，dubbo消费者。包括jwt、物流查询、图像文字提取等功能。
+ app-crawl：RPC模块，dubbo提供者。包括网络爬虫、多线程ip提取、rabbitMQ异步消息等功能。
+ app-api: dubbo RPC连接模块。也包括公用的工具。
+ app-client: web的服务端（暂不开发）
+ app-server: web端RPC的提供者(暂不开发)
+ app-admin: web的admin端（暂不开发）

## 技术选型
+ web框架： springboot 2.1.3 + mybatis 3.4.6 + druid 1.1.12
+ RPC: dubbo 2.6.5 + zookeeper 3.4.9
+ 消息的中间件：rabbitMq
+ 数据库：mysql 8
+ 缓存：redis（单机版，以后改成集群）
+ 其他：logback、lombok、flyway