# Password Manager

一个基于 Java 8 + Swing 的桌面密码管理器，适合本地保存和管理常用账号密码。

项目提供两种存储方式：

- MySQL 数据库模式
- Excel 轻量模式

用户通过密钥登录进入主界面后，可以完成账号查询、复制、编辑、删除、导入导出等常见操作。

## 功能

- 密钥登录与本地密钥文件管理
- 账号密码的查询、复制、增删改
- MySQL / Excel 双存储模式切换
- 多语言与主题配置
- 数据导入、导出、定时导出

## 技术栈

- Java 8
- Swing
- Maven
- MySQL Connector/J
- EasyExcel
- Hutool
- SLF4J + Logback

## 快速开始

环境要求：

- JDK 8
- Maven 3.x
- Windows

项目入口类：

```text
com.crane.PmApplication
```

本地开发可直接在 IDE 中运行该主类。

## 构建与测试

编译并打包：

```bash
mvn clean package
```

运行测试：

```bash
mvn test
```

运行单个测试类：

```bash
mvn -Dtest=Test test
```

## 存储模式

### MySQL 模式

适合需要数据库持久化管理的场景。

- 相关类：`AccountDao`、`JdbcConnection`
- 配置目录：`src/main/resources/config/jdbc`

### Excel 轻量模式

适合不依赖数据库、偏本地化的使用场景。

- 相关类：`LightDao`、`LightService`
- 数据目录：`src/main/resources/light_weight_data`

## 配置说明

默认配置文件：

- `src/main/resources/config/defaultConfig.properties`

可调参数文件：

- `src/main/resources/config/configurable.properties`

主要配置项包括：

- 语言
- 主题
- 默认存储模式
- 本地 / 服务器模式
- 实时搜索开关
- 生成密码长度
- 是否启用定时导出

## 项目结构

```text
src/main/java/com/crane
  PmApplication.java         程序入口
  model/                     数据与业务逻辑
  view/frame/                Swing 界面
  view/service/              界面相关服务

src/main/resources
  config/                    配置文件
  img/                       图片资源
  light_weight_data/         轻量模式数据
```

## 注意事项

- 这是桌面应用，不是 Web 项目。
- 当前开发模式下，`JdbcConnection.IS_TEST` 固定为 `true`，资源从 `src/main/resources` 读取。
- 项目对 Windows 依赖较强，包含 `attrib` 命令和 Windows 风格路径处理。

## 许可证

详见 [LICENSE](LICENSE)。
