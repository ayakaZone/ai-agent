## AI Agent 旅程规划智能体

**一个基于 Spring Boot + Spring AI + 前后端分离的智能 AI Agent 系统**

本项目实现了一个可扩展的 AI 智能体平台，集成大模型对话、RAG 检索增强、工具调用、多种流式输出形式（SSE / SseEmitter），并提供前端可视化聊天界面，**专注于智能旅程规划场景**，适合学习与二次开发。

------

### 功能概览

- **旅程规划智能体（TripPlanner）**
	- 用户只需描述目的地、天数、偏好（如“带小孩”“穷游”），AI 自动规划并输出**完整 PDF 旅程规划**（含城市概览、景点、美食、交通、住宿、每日行程）。
	- 支持同步对话与流式对话（SSE、SseEmitter），实时展示规划进度。
- **工具调用能力（Tools）**
	- 文件读写与管理（`FileOperationTool`）。
	- **PDF 生成与导出**（`PDFGenerationTool`）—— 直接输出结构化旅行手册。
	- 网络资源下载（`ResourceDownloadTool`）。
	- 网页内容抓取（`ScrapeWebPageTool`）—— 爬取景点、美食攻略。
	- 终端命令执行（`TerminalOperationTool`）。
	- **Web 搜索**（`WebSearchTool`）—— 实时获取目的地信息。
	- **图片搜索**（`PexelsImageTool`）—— 为规划嵌入景点/美食图片（基于 MCP 协议）。
- **RAG 检索增强**
	- 基于 PostgreSQL + pgvector 的向量数据库（`PgVectorVectorStoreConfig`）。
	- 内置旅游知识文档（`document/` 目录），支持自定义加载与切分、关键词增强，提升推荐答案的准确性。
- **多轮对话与记忆持久化**
	- 基于 Kryo 序列化的文件型聊天记忆（`FileBasedChatMemory`），支持多会话保存用户偏好（如“我喜欢民宿”“不吃辣”）。
- **前端聊天界面**
	- `ai-agent-frontend` 子项目，基于 Vue + Vite。
	- 提供旅程规划专属聊天界面，支持流式展示规划结果、PDF 下载按钮。

------

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

------

### 目录结构（简要）

后端（当前项目）：

- `pom.xml`：后端 Maven 配置，包含 Spring Boot、Spring AI、RAG、工具等依赖。
- `Dockerfile`：后端打包与运行镜像配置（JDK 21 + Maven，端口 8123）。
- `src/main/java/com/ayaka/aiagent`
	- `AiAgentApplication`：Spring Boot 启动类。
	- `controller/AiController`：AI 对话接口（旅程规划专用）。
	- `agent/`：智能体基类、ReActAgent、ToolCallAgent、TripPlannerAgent 等核心逻辑。
	- `app/`：`TripPlanner` 等具体应用级智能体。
	- `tools/`：各类 Tool 实现与注册（文件、PDF、爬虫、终端、搜索、图片等）。
	- `rag/`：RAG 文档加载、向量库配置、查询增强等。
	- `chatmemory/`：文件型聊天记忆实现。
	- `common/`、`exception/`、`config/` 等：通用返回体、异常处理、CORS 配置等。
- `src/main/resources`
	- `application.yml` / `application-local.yml` / `application-prod.yml`：环境配置（数据库、大模型 Key、端口等）。
	- `document/`：RAG 使用的旅游知识文档（景点、美食、交通等）。
	- `mcp-servers.json`：MCP Client 相关配置（可选）。

前端（子项目）：

- `ai-agent-frontend/`
	- `src/App.vue`、`views/`：主页面、旅程规划聊天页。
	- `src/utils/api.js`：前端调用后端 AI 接口的封装，支持流式接收规划文本并触发 PDF 下载。

------

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

	bash

	```
	mvn spring-boot:run
	```

	

	默认端口：**8123**。

------

### 前端启动（可选）

1. 进入前端目录：

	bash

	```
	cd ai-agent-frontend
	npm install
	npm run dev
	```

	

2. 在前端配置中确认后端接口地址（通常在 `src/utils/api.js`），保持与后端端口一致。

------

### 核心接口说明（后端）

所有接口均基于 `chatId` 区分会话，实现多轮记忆（如用户偏好持续保留）。

- **旅程规划智能体（TripPlanner）**
	- 同步对话：
		- `GET /ai/trip/chat/sync?message=我想4天3晚去成都&chatId=...`
	- SSE 流式（纯文本流）：
		- `GET /ai/trip/chat/sse?message=...&chatId=...`
	- SSE 流式（`ServerSentEvent` 格式）：
		- `GET /ai/trip/chat/server_sent_event?message=...&chatId=...`
	- `SseEmitter` 流式（推荐前端实时展示规划过程）：
		- `GET /ai/trip/chat/sse_emitter?message=...&chatId=...`
- **PDF 规划直接生成（可选独立接口）**
	- `POST /ai/trip/export/pdf`：接收已完成的规划 JSON，返回 PDF 文件流。

> 注：`chatId` 用于区分会话，实现多轮记忆（例如用户第二次说 “预算降低到3000”，AI 会根据历史调整规划）；不传则可能使用新的会话。

------

### Docker & 部署

- 根目录已提供 `Dockerfile`，用于在容器环境中构建后端镜像：
	- 基于 `maven:3.9-amazoncorretto-21` 构建 Jar。
	- 容器内以 `java -jar ... --spring.profiles.active=prod` 启动。
	- 暴露端口 **8123**。
- 在微信云托管或其他平台部署时：
	- 将健康检查路径配置为 `/actuator/health`（已在 prod 环境中启用 Actuator）。
	- 端口保持 8123 或与平台配置保持一致。

------

### 后续拓展方向

- 增加更多旅游相关工具（如机票/酒店 API 查询、天气查询、地图路线可视化）。
- 支持多人协作规划（共享会话）。
- 优化 RAG 旅游知识库，接入实时景点开放信息、用户评价。
- 对接更多大模型供应商（如 OpenAI、Moonshot、DeepSeek 等）。

------

### 许可证

仅记录个人学习使用
