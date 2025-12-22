<template>
  <div class="app-shell">
    <main class="app-main">
      <router-view />
    </main>
    <footer class="app-footer">
      <div class="footer-left">
        <span class="brand">AI Agent Hub</span>
        <span class="dot">•</span>
        <span>打造多应用统一体验</span>
      </div>
      <div class="footer-right">
        <span>©2025 AI Agent Hub</span>
        <span class="dot">•</span>
        <span>Inspired by <a href="#" target="_blank" rel="noreferrer">ivv 个人学习网站</a></span>
      </div>
    </footer>
  </div>
</template>

<script setup>
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
}

.app-footer {
  background: #0b1018;
  border-top: 1px solid var(--border);
  color: var(--text-muted);
  padding: 16px 28px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.footer-left,
.footer-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.brand {
  color: var(--text);
  font-weight: 700;
}

.dot {
  color: var(--primary);
}

.app-footer a {
  color: var(--primary);
}

.app-footer a:hover {
  color: #9c88ff;
}

@media (max-width: 768px) {
  .app-footer {
    flex-direction: column;
    gap: 6px;
    text-align: center;
  }
}
</style>

