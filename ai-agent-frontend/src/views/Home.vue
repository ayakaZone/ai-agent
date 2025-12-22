<template>
  <div class="home-container">
    <div class="stars-layer">
      <div v-for="i in 50" :key="i" class="star" :style="getStarStyle(i)"></div>
    </div>
    <div class="nav">
      <div class="logo">AI Agent Hub</div>
      <div class="nav-actions">
        <span class="pill">极客暗黑</span>
        <span class="pill green">SSE Realtime</span>
      </div>
    </div>

    <div class="header">
      <p class="eyebrow">Dual Apps · Unified Flow</p>
      <h1>一站式 AI 多应用中枢</h1>
      <p class="subtitle">AI 生活对谈导师：情绪陪伴与心理疏导 | AI 超级智能体：多工具链协作与任务分解</p>
      <div class="cta">
        <button class="primary" @click="goToLifeApp">开启生活导师</button>
        <button class="ghost" @click="goToManus">召唤超级智能体</button>
      </div>
    </div>
    
    <div class="apps-grid">
      <div class="app-card" @click="goToLifeApp">
        <div class="badge">情绪陪伴</div>
        <div class="app-icon">💬</div>
        <h2>AI 生活对谈导师</h2>
        <p>陪伴、倾听、疏导，SSE 流式实时回应，治愈你的每一句低语。</p>
        <div class="tag-bar">
          <span>情绪洞察</span>
          <span>关系建议</span>
          <span>实时陪伴</span>
        </div>
      </div>
      
      <div class="app-card" @click="goToManus">
        <div class="badge badge-pink">多工具链</div>
        <div class="app-icon">🤖</div>
        <h2>AI 超级智能体</h2>
        <p>拆解任务，步步执行，每个 step 独立呈现，清晰可追踪。</p>
        <div class="tag-bar">
          <span>步骤分解</span>
          <span>工具协作</span>
          <span>流式输出</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { onMounted } from 'vue'

const router = useRouter()

const goToLifeApp = () => {
  router.push('/life-app')
}

const goToManus = () => {
  router.push('/manus')
}

// 生成星星的随机样式
const getStarStyle = (index) => {
  const size = Math.random() * 2 + 1 // 1-3px
  const left = Math.random() * 100 // 0-100%
  const top = Math.random() * 100 // 0-100%
  const delay = Math.random() * 3 // 0-3s
  const duration = Math.random() * 2 + 2 // 2-4s
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    top: `${top}%`,
    animationDelay: `${delay}s`,
    animationDuration: `${duration}s`
  }
}

onMounted(() => {
  document.title = 'AI Agent Hub | 极客风多应用中心'
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: radial-gradient(circle at 10% 20%, rgba(124, 93, 255, 0.18), transparent 30%),
    radial-gradient(circle at 90% 0%, rgba(25, 227, 177, 0.12), transparent 35%),
    #0c0f14;
  padding: 36px 24px 64px;
  color: var(--text);
  position: relative;
  overflow: hidden;
}

.stars-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.star {
  position: absolute;
  background: #fff;
  border-radius: 50%;
  box-shadow: 0 0 6px rgba(255, 255, 255, 0.8),
              0 0 12px rgba(124, 93, 255, 0.6),
              0 0 18px rgba(124, 93, 255, 0.4);
  animation: twinkle infinite ease-in-out;
}

@keyframes twinkle {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
  }
}

.nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 36px;
  position: relative;
  z-index: 1;
}

.logo {
  font-weight: 800;
  letter-spacing: 1px;
  font-size: 18px;
}

.nav-actions {
  display: flex;
  gap: 10px;
}

.pill {
  padding: 6px 12px;
  border-radius: 12px;
  border: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.04);
  color: var(--text);
  font-size: 12px;
}

.pill.green {
  background: rgba(25, 227, 177, 0.12);
  border-color: rgba(25, 227, 177, 0.4);
  color: #a6ffe2;
}

.header {
  text-align: center;
  margin-bottom: 46px;
  position: relative;
  z-index: 1;
}

.eyebrow {
  color: var(--text-muted);
  letter-spacing: 2px;
  text-transform: uppercase;
  font-size: 12px;
  margin-bottom: 10px;
}

.header h1 {
  font-size: 44px;
  margin-bottom: 12px;
  letter-spacing: 1px;
}

.subtitle {
  font-size: 16px;
  color: var(--text-muted);
  margin-bottom: 22px;
}

.cta {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.primary,
.ghost {
  padding: 12px 22px;
  border-radius: 14px;
  font-weight: 700;
  cursor: pointer;
  border: 1px solid transparent;
  transition: transform 0.2s, box-shadow 0.2s;
}

.primary {
  background: linear-gradient(135deg, #7c5dff 0%, #19e3b1 100%);
  color: #fff;
  box-shadow: var(--glow);
  border-color: rgba(255, 255, 255, 0.08);
}

.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 26px rgba(124, 93, 255, 0.4);
}

.ghost {
  background: transparent;
  color: var(--text);
  border-color: var(--border);
}

.ghost:hover {
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.2);
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  max-width: 1024px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.app-card {
  background: var(--bg-panel);
  border-radius: var(--radius-lg);
  padding: 32px 28px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: var(--shadow-soft);
  position: relative;
  overflow: hidden;
  border: 1px solid var(--border);
}

.app-card::after {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 20% 20%, rgba(124, 93, 255, 0.12), transparent 35%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.app-card:hover::after {
  opacity: 1;
}

.app-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-strong);
  border-color: rgba(124, 93, 255, 0.3);
}

.badge {
  position: absolute;
  top: 16px;
  right: 16px;
  padding: 6px 12px;
  background: rgba(124, 93, 255, 0.15);
  border: 1px solid rgba(124, 93, 255, 0.4);
  color: #cbb5ff;
  border-radius: 999px;
  font-size: 12px;
}

.badge-pink {
  background: rgba(240, 147, 251, 0.15);
  border-color: rgba(240, 147, 251, 0.4);
  color: #ffd3ff;
}

.app-icon {
  font-size: 42px;
  margin-bottom: 18px;
}

.app-card h2 {
  font-size: 22px;
  margin-bottom: 12px;
  color: var(--text);
}

.app-card p {
  color: var(--text-muted);
  line-height: 1.7;
  margin-bottom: 16px;
}

.tag-bar {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag-bar span {
  padding: 6px 10px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid var(--border);
  font-size: 12px;
  color: var(--text);
}

@media (max-width: 768px) {
  .header h1 {
    font-size: 30px;
  }
  
  .apps-grid {
    grid-template-columns: 1fr;
  }
  
  .cta {
    flex-direction: column;
  }
  
  .nav {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>

