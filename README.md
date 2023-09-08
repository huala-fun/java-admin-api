## java-admin-api


### 环境搭建
#### Docker 安装 postgres
拉取镜像
```shell
docker pull postgres
```
运行容器

```shell
docker run --name postgres-server -e POSTGRES_PASSWORD=P@ssw0rd -d -p 5432:5432 postgres
```