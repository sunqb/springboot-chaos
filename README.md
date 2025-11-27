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
├── chaos-web             # 用户端接口模块（控制器、配置）
├── chaos-manager         # 运营管理平台模块（后台管理）
└── sql/                  # 数据库脚本
```

## 模块说明

### chaos-common
公共模块，包含：
- 常量定义（Constants）
- 枚举类（ResultCode）
- 自定义异常（BusinessException）
- 统一响应封装（AjaxResult）
- 工具类（DateTimeUtil、StringKit）

### chaos-service
业务服务层，包含：
- 实体类（DTO/BO/VO）
- MyBatis Mapper 接口
- Service 接口和实现
- 基础模块：用户、管理员、角色、菜单、组织机构、配置

### chaos-web
用户端 Web 接口模块（端口：8080），包含：
- 用户认证（登录/登出/验证码）
- 用户管理接口
- XSS 过滤器
- 全局异常处理

### chaos-manager
运营管理平台模块（端口：8081），包含：
- 管理员认证
- 管理员管理
- 角色管理
- 菜单管理
- 组织机构管理
- 系统配置管理

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

编辑配置文件，配置数据库和Redis连接：
- 用户端: `chaos-web/src/main/resources/application-dev.yml`
- 管理端: `chaos-manager/src/main/resources/application-dev.yml`

### 4. 启动项目

```bash
# 编译打包
mvn clean install -DskipTests

# 启动用户端（端口8080）
cd chaos-web
mvn spring-boot:run

# 启动管理端（端口8081）
cd chaos-manager
mvn spring-boot:run
```

### 5. 访问

- 用户端 API文档: http://localhost:8080/doc.html
- 管理端 API文档: http://localhost:8081/doc.html

## 默认账号

| 端口 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 8080 | admin | admin123 | 用户端登录 |
| 8081 | admin | admin123 | 管理端登录 |

## API接口

### 用户端接口 (8080)

| 接口 | 方法 | 说明 |
|------|------|------|
| /auth/login | POST | 登录 |
| /auth/logout | GET | 登出 |
| /auth/info | GET | 获取当前用户信息 |
| /auth/captcha | GET | 获取验证码 |
| /user/list | GET | 用户列表 |
| /user/{id} | GET | 用户详情 |

### 管理端接口 (8081)

| 接口 | 方法 | 说明 |
|------|------|------|
| /auth/login | POST | 管理员登录 |
| /auth/logout | GET | 管理员登出 |
| /admin/list | GET | 管理员列表 |
| /role/list | GET | 角色列表 |
| /menu/tree | GET | 菜单树 |
| /organization/tree | GET | 组织机构树 |
| /config/list | GET | 配置列表 |

## 功能特性

- [x] Sa-Token 认证授权
- [x] 统一响应封装
- [x] 全局异常处理
- [x] XSS 过滤
- [x] 图形验证码
- [x] Knife4j API文档
- [x] 多环境配置（dev/prod）
- [x] 用户管理
- [x] 管理员管理
- [x] 角色管理（RBAC）
- [x] 菜单管理
- [x] 组织机构管理
- [x] 系统配置

## License

MIT
