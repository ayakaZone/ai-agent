# AI Agent Frontend

Vue3 前端项目，用于AI Agent应用。

## 功能特性

1. **主页**：应用选择页面，可以切换不同的AI应用
2. **AI 生活对谈导师**：聊天室风格的对话界面，使用SSE实时通信
3. **AI 超级智能体**：智能体对话界面，使用SSE实时通信

## 技术栈

- Vue 3
- Vue Router 4
- Axios
- Vite

## 安装依赖

```bash
npm install
```

## 开发运行

```bash
npm run dev
```

项目将在 http://localhost:5173 启动

## 构建生产版本

```bash
npm run build
```

## 项目结构

```
src/
├── views/           # 页面组件
│   ├── Home.vue     # 主页
│   ├── LifeAppChat.vue  # AI生活对谈导师
│   └── ManusChat.vue    # AI超级智能体
├── router/          # 路由配置
├── utils/           # 工具函数
│   ├── request.js   # Axios配置
│   └── sse.js       # SSE连接工具
├── styles/          # 全局样式
└── main.js          # 入口文件
```

## 后端接口

默认后端地址：http://localhost:8123/api

- `/ai/life_app/chat/sse` - AI生活对谈导师SSE接口
- `/ai/manus/chat` - AI超级智能体SSE接口

