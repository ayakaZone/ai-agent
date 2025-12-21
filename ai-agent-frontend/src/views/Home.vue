<template>
  <div class="home-container">
    <div class="header">
      <h1 class="title-glow">AI Agent 应用中心</h1>
      <p class="subtitle">选择您要使用的AI应用</p>
    </div>
    <div class="apps-grid">
      <div class="app-card" @click="goToLifeApp">
        <div class="app-icon">💬</div>
        <h2>AI 生活对谈导师</h2>
        <p>智能对话助手，为您的生活提供专业建议和指导</p>
      </div>
      <div class="app-card" @click="goToManus">
        <div class="app-icon">🤖</div>
        <h2>AI 超级智能体</h2>
        <p>强大的AI智能体，支持多种工具和功能</p>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import Footer from '../components/Footer.vue'

const router = useRouter()

const goToLifeApp = () => {
  router.push('/life-app')
}

const goToManus = () => {
  router.push('/manus')
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0d0d0d 0%, #1a1a1a 50%, #0d0d0d 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  overflow: hidden;
}

.home-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 50%, rgba(0, 212, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(0, 255, 136, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

.header {
  text-align: center;
  color: #fff;
  margin-bottom: 60px;
  position: relative;
  z-index: 1;
}

.title-glow {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 15px;
  background: linear-gradient(135deg, #00d4ff 0%, #00ff88 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 30px rgba(0, 212, 255, 0.5);
  letter-spacing: -1px;
}

.subtitle {
  font-size: 1.2rem;
  color: #999;
  font-weight: 300;
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 400px));
  gap: 30px;
  width: 100%;
  max-width: 900px;
  position: relative;
  z-index: 1;
}

.app-card {
  background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
  border: 1px solid #333;
  border-radius: 16px;
  padding: 40px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.app-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.1), transparent);
  transition: left 0.5s;
}

.app-card:hover::before {
  left: 100%;
}

.app-card:hover {
  transform: translateY(-8px);
  border-color: #00d4ff;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(0, 212, 255, 0.2);
}

.app-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  filter: drop-shadow(0 0 10px rgba(0, 212, 255, 0.3));
}

.app-card h2 {
  font-size: 1.8rem;
  color: #fff;
  margin-bottom: 15px;
  font-weight: 600;
}

.app-card p {
  font-size: 1rem;
  color: #999;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .title-glow {
    font-size: 2.5rem;
  }
  
  .apps-grid {
    grid-template-columns: 1fr;
  }
  
  .home-container {
    padding: 20px 15px;
  }
}
</style>


