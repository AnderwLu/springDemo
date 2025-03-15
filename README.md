# Spring Boot与Vue集成的演示项目

这是一个基于Spring Boot与Vue的前后端不分离架构的演示项目，展示了如何在Spring Boot项目中集成Vue.js，实现现代化的用户界面和交互体验。

## 技术栈

### 后端
- Spring Boot 2.7.5
- Spring Security
- Spring Data JPA
- Spring WebSocket
- Thymeleaf
- MySQL
- Redis
- Lombok

### 前端
- Vue.js 3
- Bootstrap 5
- HTML5/CSS3

## 功能特性

- 用户认证与授权
- 基于角色的权限控制
- 用户管理
- 角色管理
- 权限管理
- 消息通知
- 实时通信（WebSocket）
- 数据缓存（Redis）
- 响应式界面设计

## 项目结构

```
src/main/java/com/example/springdemo/
├── config/                 # 配置类
├── controller/             # 控制器
├── entity/                 # 实体类
├── repository/             # 数据访问层
├── service/                # 服务层
│   └── impl/               # 服务实现
├── common/                 # 通用组件
│   ├── exception/          # 异常处理
│   ├── result/             # 统一返回结果
│   └── util/               # 工具类
└── SpringDemoApplication.java  # 应用程序入口

src/main/resources/
├── static/                 # 静态资源
├── templates/              # 模板文件
└── application.yml         # 应用配置文件
```

## 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 数据库配置
1. 创建MySQL数据库：`springdemo`
2. 修改`application.yml`中的数据库连接信息

### 运行应用
```bash
# 克隆项目
git clone https://github.com/yourusername/springdemo.git

# 进入项目目录
cd springdemo

# 编译打包
mvn clean package

# 运行应用
java -jar target/springdemo-0.0.1-SNAPSHOT.jar
```

访问：http://localhost:8080/springdemo

### 默认账号
- 用户名：admin
- 密码：123456

## 开发指南

### 添加新功能
1. 创建实体类
2. 创建Repository接口
3. 创建Service接口和实现类
4. 创建Controller
5. 创建前端页面

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一使用Result返回结果
- 统一异常处理

## 贡献指南
欢迎提交Issue和Pull Request，一起完善这个项目！

## 许可证
MIT
