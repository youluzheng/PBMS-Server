# PBMS-Server

图片管理系统服务端

## feature

- [x] 图片水印(文字、图片)
- [x] 图片压缩(比例)
- [x] 上传图片限制(大小、类型)
- [x] 图片存储(多级目录)
- [x] HTTPS支持

## 贡献代码

**[如何协作开发](https://gitea.965.life/PBMS/PBMS-Server/wiki/%E5%A6%82%E4%BD%95%E5%8D%8F%E4%BD%9C%E5%BC%80%E5%8F%91)**

## install

1. 源码构建

```sh
./mvnw clean package
jar -jar pbms-server-x.x.x-RELEASE.jar
```

2. 使用打包好的jar

```sh
jar -jar pbms-server-x.x.x-RELEASE.jar
```

3. [docker](https://gitea.965.life/PBMS/PBMS-Server/wiki/docker)
