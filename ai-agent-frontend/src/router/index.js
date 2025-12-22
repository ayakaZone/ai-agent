import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LifeAppChat from '../views/LifeAppChat.vue'
import ManusChat from '../views/ManusChat.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/life-app',
    name: 'LifeAppChat',
    component: LifeAppChat
  },
  {
    path: '/manus',
    name: 'ManusChat',
    component: ManusChat
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

