# 财务管理系统

一个基于 Spring Boot + Vue 3 的企业财务管理系统，支持会计科目管理、凭证录入、财务报表生成、银行对账等功能。

## 📋 功能特性

### 总账管理
- ✅ 公司信息管理
- ✅ 会计科目管理（支持多级科目）
- ✅ 会计凭证录入与管理
- ✅ 财务报表（资产负债表、利润表、现金流量表）

### 业务模块
- ✅ 供应商管理
- ✅ 采购订单管理
- ✅ 客户管理
- ✅ 销售发票管理
- ✅ 员工管理
- ✅ 费用报销管理

### 银行对账
- ✅ 银行流水导入
- ✅ 自动匹配对账
- ✅ 对账报告生成

### 数据看板
- ✅ 实时业务统计
- ✅ 待处理事项提醒
- ✅ 数据可视化展示

## 🛠️ 技术栈

### 后端
- Spring Boot 2.7.18
- Spring Data JPA
- MySQL 8.0
- Maven
- Java 1.8

### 前端
- Vue 3
- Element Plus
- Vue Router
- Axios
- Vite

## 📦 项目结构

```
财务管理系统/
├── backend/                    # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/finance/
│   │       │       ├── common/         # 通用类
│   │       │       ├── config/         # 配置类
│   │       │       ├── controller/     # 控制器
│   │       │       ├── entity/         # 实体类
│   │       │       ├── repository/     # 数据访问层
│   │       │       └── service/        # 业务逻辑层
│   │       └── resources/
│   │           ├── application.yml.example  # 配置示例
│   │           └── application.yml          # 实际配置（不上传）
│   └── pom.xml                # Maven 配置
│
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/               # API 接口
│   │   ├── router/            # 路由配置
│   │   ├── utils/             # 工具函数
│   │   ├── views/             # 页面组件
│   │   ├── App.vue            # 根组件
│   │   └── main.js            # 入口文件
│   ├── package.json           # NPM 配置
│   └── vite.config.js         # Vite 配置
│
├── database/                   # 数据库脚本
│   └── init.sql               # 初始化脚本
│
├── start-backend.bat          # 后端启动脚本（Windows）
├── start-frontend.bat         # 前端启动脚本（Windows）
└── README.md                  # 项目说明

```

## 🚀 快速开始

### 环境要求

- JDK 1.8 或以上
- MySQL 5.7 或 8.0
- Node.js 16+ 和 NPM
- Maven 3.6+

### 1. 克隆项目

```bash
git clone https://github.com/liangchen67/caiwuguanlixitong.git
cd caiwuguanlixitong
```

### 2. 数据库配置

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE finance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本
mysql -u root -p finance_db < database/init.sql
```

### 3. 后端配置

复制配置文件并修改数据库连接信息：

```bash
cd backend/src/main/resources
cp application.yml.example application.yml
```

编辑 `application.yml`，修改数据库密码：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/finance_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 你的数据库密码  # 修改这里
```

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

或使用启动脚本：
```bash
.\start-backend.bat  # Windows
```

后端将运行在 `http://localhost:8080`

### 5. 启动前端

```bash
cd frontend
npm install
npm run dev
```

或使用启动脚本：
```bash
.\start-frontend.bat  # Windows
```

前端将运行在 `http://localhost:3000`

### 6. 访问系统

打开浏览器访问：`http://localhost:3000`

## 🌐 生产环境部署

详细的 Windows 服务器部署指南请参考项目文档。

### 简要步骤：

1. **构建后端**
```bash
cd backend
mvn clean package
```

2. **构建前端**
```bash
cd frontend
npm run build
```

3. **部署**
- 后端：将 JAR 包部署到服务器，配置为 Windows 服务
- 前端：将 dist 目录部署到 Nginx
- 数据库：确保 MySQL 已安装并导入初始化脚本

## 📖 API 文档

### 后端 API 端点

- **公司管理**: `/api/company/*`
- **会计科目**: `/api/account-subject/*`
- **会计凭证**: `/api/journal-entry/*`
- **财务报表**: `/api/report/*`
- **供应商管理**: `/api/supplier/*`
- **采购订单**: `/api/purchase-order/*`
- **客户管理**: `/api/customer/*`
- **销售发票**: `/api/sales-invoice/*`
- **员工管理**: `/api/employee/*`
- **费用报销**: `/api/employee-expense/*`
- **银行流水**: `/api/bank-statement/*`
- **银行对账**: `/api/bank-reconciliation/*`
- **数据看板**: `/api/dashboard/*`

## 🔒 安全说明

- ⚠️ **请勿将 `application.yml` 上传到公开仓库**
- ⚠️ **生产环境请使用强密码**
- ⚠️ **建议配置 HTTPS**
- ⚠️ **定期备份数据库**

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

## 👨‍💻 作者

**李小彬** - [liangchen67](https://github.com/liangchen67)

## 🙏 致谢

感谢所有使用和贡献这个项目的开发者！

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue: https://github.com/liangchen67/caiwuguanlixitong/issues
- 服务器地址: 121.41.131.161

---

⭐ 如果这个项目对你有帮助，欢迎 Star！




