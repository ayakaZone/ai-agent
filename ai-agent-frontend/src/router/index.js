import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LifeAppChat from '../views/LifeAppChat.vue'
import ManusChat from '../views/ManusChat.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: 'AI Agent - 专业的AI智能体应用平台',
      description: 'AI Agent应用中心 - 提供AI生活对谈导师和AI超级智能体等智能服务',
      keywords: 'AI智能体,人工智能,AI对话,AI助手,智能体应用'
    }
  },
  {
    path: '/life-app',
    name: 'LifeAppChat',
    component: LifeAppChat,
    meta: {
      title: 'AI生活对谈导师 - AI Agent',
      description: 'AI生活对谈导师 - 智能对话助手，为您的生活提供专业建议和指导',
      keywords: 'AI对话,AI助手,生活咨询,智能导师,AI生活助手'
    }
  },
  {
    path: '/manus',
    name: 'ManusChat',
    component: ManusChat,
    meta: {
      title: 'AI超级智能体 - AI Agent',
      description: 'AI超级智能体 - 强大的AI智能体，支持多种工具和功能，分步骤思考执行任务',
      keywords: 'AI智能体,AI Agent,智能助手,AI工具,分步骤AI'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：设置页面标题和SEO
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
  
  // 设置meta description
  if (to.meta.description) {
    let metaDescription = document.querySelector('meta[name="description"]')
    if (!metaDescription) {
      metaDescription = document.createElement('meta')
      metaDescription.setAttribute('name', 'description')
      document.head.appendChild(metaDescription)
    }
    metaDescription.setAttribute('content', to.meta.description)
  }
  
  // 设置meta keywords
  if (to.meta.keywords) {
    let metaKeywords = document.querySelector('meta[name="keywords"]')
    if (!metaKeywords) {
      metaKeywords = document.createElement('meta')
      metaKeywords.setAttribute('name', 'keywords')
      document.head.appendChild(metaKeywords)
    }
    metaKeywords.setAttribute('content', to.meta.keywords)
  }
  
  next()
})

export default router


