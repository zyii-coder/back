# 失物招领系统 · Lost and Found System

一个前后端分离的校园失物招领平台示例：后端 **Spring Boot（REST API）**，前端 **Vue 3 + Vite（SPA）**。
项目根目录采用 **monorepo** 结构，前后端各自独立、可单独构建部署。

```
LostAndFoundSystem/
├── backend/     # Spring Boot 工程（Maven，含 Maven Wrapper）
├── frontend/    # Vue 3 + Vite 工程（npm）
└── README.md
```

---

## 1. 如何运行

### 后端（端口 8080）

> 本仓库已内置 **Maven Wrapper**，无需在电脑上单独安装 Maven。
> 首次运行会自动下载 Maven 3.9.9 与依赖（需联网）。

```bash
cd backend

# Windows
mvnw.cmd spring-boot:run
# macOS / Linux
./mvnw spring-boot:run
```

启动后：

- API 根路径：`http://localhost:8080/api`
- H2 控制台（开发库）：`http://localhost:8080/h2-console`
  （JDBC URL：`jdbc:h2:mem:lostfound`，用户名 `sa`，密码空）

首次启动会**自动写入演示数据**（仅当数据库为空时）：

| 账号 | 密码 | 角色 |
| --- | --- | --- |
| `demo` | `demo123` | USER |
| `admin` | `admin123` | ADMIN |

以及 3 条示例失物/招领记录，方便你直接在前端体验。

### 前端（端口 5173）

```bash
cd frontend
npm install
npm run dev
```

打开 `http://localhost:5173`。浏览器访问 `/api/*` 时，Vite 开发服务器会把请求**代理**到 `http://localhost:8080`，因此开发阶段无需处理跨域。

> 构建生产包：`npm run build`（产物在 `frontend/dist/`）。

---

## 2. 技术选型与理由（How & Why）

### 为什么是「前后端分离 + monorepo」

- **关注点分离**：后端只产出纯 JSON API，前端只负责渲染。两边可以各自选型、各自部署（后端打成 jar，前端静态托管到 Nginx / CDN / 对象存储）。
- **monorepo 同仓**：对于课程项目 / 小团队，把 `backend` 和 `frontend` 放在一个仓库里，便于统一版本、统一 CI、统一阅读，又不强耦合（各自有独立构建）。
- **接口契约稳定**：前后端通过 `/api` 下的 REST 约定通信，前端用 Vite 代理或同源部署规避 CORS。

### 后端：Spring Boot 3.4

| 选型 | 为什么 |
| --- | --- |
| **Spring Boot** | 约定优于配置，内嵌 Tomcat，`spring-boot-starter-*` 一站式整合 Web / JPA / Security，启动即用。 |
| **Spring Data JPA (Hibernate)** | 用 `@Entity` + `JpaRepository` 即可获得 CRUD 与分页，避免手写大量 JDBC/SQL。 |
| **H2（开发）/ MySQL（生产）** | 默认 H2 内存库让项目「零配置即可跑起来」；通过 `mysql` profile（`--spring.profiles.active=mysql`）一键切到 MySQL，无需改代码。 |
| **Spring Security + JWT** | 无状态鉴权：登录拿 token，之后每个请求带 `Authorization: Bearer <token>`。比 Session 更适合前后端分离与横向扩展。 |
| **jjwt 0.12.x** | 成熟、API 清晰的 JWT 库，HS256 签名。 |
| **Bean Validation** | DTO 上用 `@Valid` + 注解做入参校验，校验失败自动返回 400 与字段级错误。 |
| **Maven Wrapper** | 团队/ CI 不必预先装 Maven，`./mvnw` 自举，保证构建环境一致。 |

> 说明：本机当前是 **JDK 25**。后端 `pom.xml` 中 `java.version=21`（LTS），在 JDK 25 上可正常编译运行；若后续升级 Spring Boot，可获得对 JDK 25 的官方完整支持。

### 后端分层结构（为什么这样拆）

```
controller   → 接收 HTTP 请求，做参数绑定与权限边界（不含业务）
service      → 业务逻辑、事务、实体↔DTO 映射
repository   → Spring Data JPA 数据访问
entity       → 持久化模型（与数据库表一一对应）
dto          → 对外契约（请求/响应），不暴露实体内部细节
config       → 安全、CORS、演示数据初始化
security     → JWT 生成/校验/过滤器
exception    → 统一异常 → 统一 JSON 错误体
```

这样拆的好处：**每一层职责单一、可独立测试**；DTO 与 Entity 分离，避免把密码等内部字段泄露给前端。

### 前端：Vue 3.5 + Vite 5

| 选型 | 为什么 |
| --- | --- |
| **Vue 3（Composition API）** | 上手快、模板直观，`<script setup>` 让组件更简洁；对课程/管理后台类 CRUD 应用非常合适。 |
| **Vite** | 冷启动与 HMR 极快，配置少；`<script type="module">` 原生 ESM 开发体验好。 |
| **Vue Router** | 前端路由 + 路由守卫（标注 `meta.requiresAuth` 的页面未登录自动跳登录）。 |
| **Pinia** | Vue 官方推荐的状态库，管理登录态/Token，并持久化到 `localStorage`。 |
| **Axios** | 统一 `baseURL='/api'`，请求拦截器自动附加 Token，响应拦截器统一错误处理与 401 跳转。 |
| **Element Plus** | 现成的表单、表格、卡片、分页、标签等组件，省去大量 UI 样式工作，产出风格统一。 |

### 前后端如何协作（关键设计点）

1. **开发跨域**：Vite `server.proxy` 把 `/api` 转发到 `:8080`，前端代码里只写相对路径 `/api/...`。
2. **生产跨域**：后端 `SecurityConfig` 已配置 CORS，允许 `localhost:5173/5174`；生产建议把前端静态资源部署到与 API 同源（或反向代理）以彻底避免跨域。
3. **鉴权流程**：`POST /api/auth/login` → 返回 JWT → 前端存入 Pinia + localStorage → 后续请求自动带上 → 后端 `JwtAuthFilter` 解析并写入安全上下文 → Controller 用 `@AuthenticationPrincipal` 拿到当前用户。
4. **统一错误**：后端任何异常经 `GlobalExceptionHandler` 转为 `{timestamp, status, error, message}`，前端 `http.js` 拦截后展示友好提示。

---

## 3. 主要接口一览

| 方法 | 路径 | 说明 | 鉴权 |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | 注册（返回 JWT） | 否 |
| POST | `/api/auth/login` | 登录（返回 JWT） | 否 |
| GET | `/api/items` | 分页搜索（支持 `type`/`category`/`status`/`keyword`） | 是 |
| GET | `/api/items/mine` | 我的发布 | 是 |
| GET | `/api/items/{id}` | 详情 | 是 |
| POST | `/api/items` | 发布 | 是 |
| PUT | `/api/items/{id}` | 修改（仅本人） | 是 |
| DELETE | `/api/items/{id}` | 删除（仅本人） | 是 |

> 浏览器可借助 H2 控制台或 Postman / curl 直接调 API；前端页面已封装好这些调用。

---

## 4. 切换到 MySQL（生产）

1. 本地/服务器准备一个 MySQL 库：`lost_found`，账号 `root/root`（或修改 `application.yml`）。
2. 启动时激活 profile：

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
   # 或打成 jar 后：
   java -jar target/lost-and-found-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=mysql
   ```

`ddl-auto: update` 会自动建表；首次运行也会写入演示数据。

---

## 5. 下一步可扩展点

- 物品图片：当前 `imageUrl` 为文本链接，可接入对象存储（OSS/S3）做实际上传。
- 管理员后台：用 `ROLE_ADMIN` 增加审核、置顶、统计等接口与页面。
- 实时通知：失主与拾得主匹配后通过站内信 / 邮件通知。
- 测试：后端已有 `SpringBootTest` 上下文加载测试，可继续补充 Service/Controller 单测。

---

## 6. 常见问题

- **启动报 `Port XXXX was already in use`**：说明 8080 被占用（可能是上一次没关掉的实例，或环境中存在同名端口变量）。
  先释放端口再启动：`netstat -ano | findstr :8080` 找到 PID 后 `taskkill /PID <pid> /F`；或临时换端口 `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090`。
  > 注意：若系统环境变量里设置了 `SERVER__PORT` / `SERVER_PORT`，Spring Boot 的宽松绑定会优先于 `application.yml` 中的 `server.port` 使用它。这是某些集成环境（如 IDE / 代理宿主进程）注入的，正常本机开发一般不会有。
- **前端 `npm run dev` 后接口 401/跨域**：确认后端已启动在 8080，且 Vite 代理（`vite.config.js` 中 `/api -> localhost:8080`）未被改动。
- **JWT 报错 `The JWT signature is invalid` / 登录拿不到 token**：检查 `application.yml` 里 `app.jwt.secret` 长度 ≥ 32 字节（默认开发值已满足）。
