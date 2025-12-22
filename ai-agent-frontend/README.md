# AI Agent Frontend

Vue3 前端项目，提供 AI 生活对谈导师和 AI 超级智能体两个聊天应用。

## 功能特性

- 🏠 **主页**：应用切换中心
- 💬 **AI 生活对谈导师**：专业的 AI 生活导师聊天应用
- 🤖 **AI 超级智能体**：强大的 AI 智能体聊天应用
- ⚡ **实时流式对话**：基于 SSE 的实时消息流
- 📱 **响应式设计**：支持移动端和桌面端

## 技术栈

- Vue 3
- Vue Router 4
- Axios
- Vite

## 项目结构

```
src/
├── components/     # 公共组件
├── router/        # 路由配置
│   └── index.js
├── styles/        # 全局样式
│   └── global.css
├── utils/         # 工具函数
│   └── api.js     # API 请求封装
├── views/         # 页面组件
│   ├── Home.vue           # 主页
│   ├── LifeAppChat.vue    # AI 生活对谈导师
│   └── ManusChat.vue      # AI 超级智能体
├── App.vue        # 根组件
└── main.js        # 入口文件
```

## 安装依赖

```bash
npm install
```

## 运行项目

```bash
npm run dev
```

项目将在 `http://localhost:5173` 启动。

## 构建项目

```bash
npm run build
```

## 后端接口

项目默认连接的后端地址：`http://localhost:8123/api`

### 接口列表

1. **AI 生活对谈导师 SSE 接口**
   - 路径：`/ai/life_app/chat/sse`
   - 方法：GET
   - 参数：
     - `message`: 用户消息
     - `chatId`: 聊天室 ID

2. **AI 超级智能体 SSE 接口**
   - 路径：`/ai/manus/chat`
   - 方法：GET
   - 参数：
     - `message`: 用户消息

## 使用说明

1. 启动后端服务（确保运行在 `http://localhost:8123`）
2. 运行前端项目：`npm run dev`
3. 在浏览器中访问 `http://localhost:5173`
4. 选择要使用的 AI 应用
5. 开始对话

## 注意事项

- 确保后端服务已启动并运行在正确的端口
- 如果后端地址不同，请修改 `src/utils/api.js` 中的 `API_BASE_URL`
- 聊天会话会自动生成唯一 ID（仅限 AI 生活对谈导师应用）

