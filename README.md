# SpringBoot-Chaos

基于 Spring Boot 3.x + Sa-Token 的轻量级脚手架项目，用于快速开发个人项目。

## 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.0 | 基础框架 |
| JDK | 17 | Java版本 |
| Sa-Token | 1.37.0 | 轻量级认证授权框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| Druid | 1.2.20 | 数据库连接池 |
| Redis | - | 缓存/Token存储 |
| Knife4j | 4.3.0 | API文档 |

## 项目结构

```
springboot-chaos/
├── chaos-common          # 公共模块（常量、异常、工具类、VO）
├── chaos-service         # 业务服务模块（实体、Mapper、Service）
├── chaos-web             # Web接口模块（控制器、配置、启动入口）
└── sql/                  # 数据库脚本
```

## 快速开始

### 1. 环境准备

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 初始化数据库

```sql
CREATE DATABASE chaos DEFAULT CHARACTER SET utf8mb4;
```

执行 `sql/init.sql` 初始化表结构

### 3. 修改配置

编辑 `chaos-web/src/main/resources/application-dev.yml`，配置数据库和Redis连接

### 4. 启动项目

```bash
mvn clean install -DskipTests
cd chaos-web
mvn spring-boot:run
```

### 5. 访问

- API文档: http://localhost:8080/doc.html
- 接口地址: http://localhost:8080

## 默认账号

- 用户名: admin
- 密码: admin123

## API接口

### 认证接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /auth/login | POST | 登录 |
| /auth/logout | GET | 登出 |
| /auth/info | GET | 获取当前用户信息 |
| /auth/captcha | GET | 获取验证码 |

### 用户管理

| 接口 | 方法 | 说明 |
|------|------|------|
| /user/list | GET | 用户列表 |
| /user/{id} | GET | 用户详情 |
| /user | POST | 新增用户 |
| /user | PUT | 更新用户 |
| /user/{id} | DELETE | 删除用户 |

## License

MIT
