## AI Agent 智能体项目

**一个基于 Spring Boot + Spring AI + 前后端分离的智能 AI Agent 系统**

本项目实现了一个可扩展的 AI 智能体平台，集成大模型对话、RAG 检索增强、工具调用、多种流式输出形式（SSE / SseEmitter），并提供前端可视化聊天界面，适合学习与二次开发。

---

### 功能概览

- **生活助手 LifeApp**
  - 提供日常问答、计划生成等对话能力（`LifeApp`）。
  - 支持普通同步对话与流式对话（SSE、`SseEmitter`）。
- **Manus 通用智能体**
  - 类 ChatGPT / Claude 风格的通用智能体（`Manus`）。
  - 基于 Spring AI 工具调用机制，自动选择/组合工具完成复杂任务。
- **工具调用能力（Tools）**
  - 文件读写与管理（`FileOperationTool`）。
  - PDF 生成与导出（`PDFGenerationTool`）。
  - 网络资源下载（`ResourceDownloadTool`）。
  - 网页内容抓取（`ScrapeWebPageTool`）。
  - 终端命令执行（`TerminalOperationTool`）。
  - Web 搜索（`WebSearchTool`）。
- **RAG 检索增强**
  - 基于 PostgreSQL + pgvector 的向量数据库（`PgVectorVectorStoreConfig`）。
  - 自定义文档加载与切分、关键词增强（`LifeAppDocumentLoader`、`MyTokenTextSplitter`、`MyKeywordEnricher` 等）。
- **聊天记忆**
  - 基于 Kryo 序列化的文件型聊天记忆（`FileBasedChatMemory`），支持多会话持久化。
- **前端聊天界面**
  - `ai-agent-frontend` 子项目，基于 Vue + Vite。
  - 提供主页、LifeApp 聊天页、Manus 智能体聊天页等视图。

---

### 技术栈

- **后端**
  - Java 21
  - Spring Boot 3.4.x
  - Spring Web
  - Spring AI（DashScope、大模型、RAG、工具调用）
  - PostgreSQL + pgvector
  - Kryo（聊天记忆序列化）
  - Jsoup、iText PDF 等第三方工具库
- **前端**
  - Vue 3 + Vite
  - Axios 等
- **基础设施 / 部署**
  - Docker 镜像构建（根目录 `Dockerfile`）
  - 适配微信云托管 / 容器环境部署

---

### 目录结构（简要）

后端（当前项目）：

- `pom.xml`：后端 Maven 配置，包含 Spring Boot、Spring AI、RAG、工具等依赖。
- `Dockerfile`：后端打包与运行镜像配置（JDK 21 + Maven，端口 8123）。
- `src/main/java/com/ayaka/aiagent`
  - `AiAgentApplication`：Spring Boot 启动类。
  - `controller/AiController`：AI 对话接口（LifeApp & Manus）。
  - `agent/`：智能体基类、ReActAgent、ToolCallAgent、Manus 等核心逻辑。
  - `app/`：`LifeApp` 等具体应用级智能体。
  - `tools/`：各类 Tool 实现与注册（文件、PDF、爬虫、终端、搜索等）。
  - `rag/`：RAG 文档加载、向量库配置、查询增强等。
  - `chatmemory/`：文件型聊天记忆实现。
  - `common/`、`exception/`、`config/` 等：通用返回体、异常处理、CORS 配置等。
- `src/main/resources`
  - `application.yml` / `application-local.yml` / `application-prod.yml`：环境配置（数据库、大模型 Key、端口等）。
  - `document/`：RAG 使用的知识文档。
  - `mcp-servers.json`：MCP Client 相关配置（可选）。

前端（子项目）：

- `ai-agent-frontend/`
  - `src/App.vue`、`views/`：主页面、LifeApp、Manus 聊天页。
  - `src/utils/api.js`：前端调用后端 AI 接口的封装。

---

### 后端快速启动（本地）

1. **准备环境**
   - JDK 21+
   - Maven 3.9+
   - 可选：PostgreSQL + pgvector（如需完整 RAG 功能）
2. **配置参数**
   - 修改 `src/main/resources/application.yml` / `application-local.yml`：
     - 数据库连接（`spring.datasource.*`）。
     - 大模型 API Key（如 DashScope 的 `spring.ai.dashscope.api-key`）。
3. **运行后端**
   ```bash
   mvn spring-boot:run
   ```
   默认端口：**8123**。

---

### 前端启动（可选）

1. 进入前端目录：
   ```bash
   cd ai-agent-frontend
   npm install
   npm run dev
   ```
2. 在前端配置中确认后端接口地址（通常在 `src/utils/api.js`），保持与后端端口一致。

---

### 核心接口说明（后端）

- **LifeApp 对话**
  - 同步：
    - `GET /ai/life_app/chat/sync?message=...&chatId=...`
  - SSE 流式（纯文本流）：
    - `GET /ai/life_app/chat/sse?message=...&chatId=...`
  - SSE 流式（`ServerSentEvent`）：
    - `GET /ai/life_app/chat/server_sent_event?message=...&chatId=...`
  - `SseEmitter` 流式：
    - `GET /ai/life_app/chat/sse_emitter?message=...&chatId=...`

- **Manus 智能体对话**
  - SSE 流式（`SseEmitter`）：
    - `GET /ai/manus/chat?message=...`

> 注：`chatId` 用于区分会话，实现多轮记忆；不传则可能使用新的会话。

---

### Docker & 部署

- 根目录已提供 `Dockerfile`，用于在容器环境中构建后端镜像：
  - 基于 `maven:3.9-amazoncorretto-21` 构建 Jar。
  - 容器内以 `java -jar ... --spring.profiles.active=prod` 启动。
  - 暴露端口 **8123**。
- 在微信云托管或其他平台部署时：
  - 将健康检查路径配置为 `/actuator/health`（已在 prod 环境中启用 Actuator）。
  - 端口保持 8123 或与平台配置保持一致。

---

### 后续拓展方向

- 增加更多自定义工具（如日历、任务管理、第三方 API 集成）。
- 扩展更多应用型智能体（如美食助手、学习助手、情感陪伴等）。
- 优化 RAG 文档管理与索引策略，接入企业知识库。
- 对接更多大模型供应商（如 OpenAI、Moonshot、DeepSeek 等）。

---

### 许可证

仅记录个人学习使用
