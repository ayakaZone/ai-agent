<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h4>AI Agent</h4>
        <p>专业的AI智能体应用平台</p>
      </div>
      <div class="footer-section">
        <h4>相关链接</h4>
        <ul>
          <li><a href="/">主页</a></li>
          <li><a href="/life-app">AI生活对谈导师</a></li>
          <li><a href="/manus">AI超级智能体</a></li>
        </ul>
      </div>
      <div class="footer-section">
        <h4>联系我们</h4>
        <ul>
          <li>商务合作</li>
          <li>技术支持</li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; {{ currentYear }} AI Agent. All rights reserved.</p>
      <p>沪ICP备xxxxxx号</p>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'

const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped>
.footer {
  background: linear-gradient(180deg, #1a1a1a 0%, #0d0d0d 100%);
  border-top: 1px solid #333;
  color: #999;
  padding: 40px 20px 20px;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  margin-bottom: 30px;
}

.footer-section h4 {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 15px;
  position: relative;
  padding-bottom: 10px;
}

.footer-section h4::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 30px;
  height: 2px;
  background: linear-gradient(90deg, #00d4ff, #00ff88);
}

.footer-section p {
  color: #999;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 10px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section ul li {
  margin-bottom: 10px;
  font-size: 14px;
}

.footer-section ul li a {
  color: #999;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section ul li a:hover {
  color: #00d4ff;
}

.footer-bottom {
  max-width: 1200px;
  margin: 0 auto;
  padding-top: 20px;
  border-top: 1px solid #333;
  text-align: center;
  font-size: 12px;
  color: #666;
}

.footer-bottom p {
  margin: 5px 0;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
  
  .footer {
    padding: 30px 15px 15px;
  }
}
</style>

