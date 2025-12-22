<template>
  <div class="chat-container">
    <div class="chat-header">
      <div class="left">
        <button class="back-btn" @click="goBack">← 返回</button>
        <div class="titles">
          <h2>AI 生活对谈导师</h2>
          <p class="subtitle">情绪陪伴 · 关系洞察 · 贴心建议</p>
        </div>
      </div>
      <div class="chat-id">会话 ID: {{ chatId }}</div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="(msg, index) in messages" 
        :key="index"
        :class="['message', msg.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="avatar" :style="{ background: msg.role === 'user' ? userAvatar.bg : aiAvatar.bg }">
          {{ msg.role === 'user' ? userAvatar.text : aiAvatar.text }}
        </div>
        <div class="message-content">
          <div class="message-text" v-html="formatMessage(msg.content)"></div>
          <div class="message-time">{{ formatTime(msg.time) }}</div>
        </div>
      </div>
      <div v-if="isLoading" class="message ai-message">
        <div class="avatar" :style="{ background: aiAvatar.bg }">{{ aiAvatar.text }}</div>
        <div class="message-content">
          <div class="message-text typing-indicator">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="chat-input-container">
      <div class="input-meta">
        <div class="pill">AI 生活导师</div>
        <div class="pill secondary">SSE 实时流</div>
      </div>
      <textarea
        v-model="inputMessage"
        @keydown.enter.exact.prevent="sendMessage"
        @keydown.shift.enter.exact="inputMessage += '\n'"
        placeholder="输入您的消息..."
        class="chat-input"
        :disabled="isLoading"
        rows="2"
        ref="inputRef"
      ></textarea>
      <button 
        @click="sendMessage" 
        class="send-btn"
        :disabled="!inputMessage.trim() || isLoading"
      >
        发送
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { chatWithLifeAppSSE } from '../utils/api'
import { getRandomUserAvatar, lifeAiAvatar } from '../utils/avatars'

const router = useRouter()
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const chatId = ref('')
const messagesContainer = ref(null)
const inputRef = ref(null)
let eventSource = null
const userAvatar = ref(getRandomUserAvatar())
const aiAvatar = ref(lifeAiAvatar)

// 生成聊天室 ID
const generateChatId = () => {
  return `life-app-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`
}

// 初始化聊天室 ID & SEO
onMounted(() => {
  chatId.value = generateChatId()
  inputRef.value?.focus()
  document.title = 'AI 生活对谈导师 | AI Agent Hub'
})

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 监听消息变化，自动滚动
watch(messages, () => {
  scrollToBottom()
}, { deep: true })

// 发送消息
const sendMessage = () => {
  if (!inputMessage.value.trim() || isLoading.value) return
  
  const userMessage = inputMessage.value.trim()
  inputMessage.value = ''
  
  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: userMessage,
    time: new Date()
  })
  
  isLoading.value = true
  
  // 创建 AI 消息占位符
  const aiMessageIndex = messages.value.length
  messages.value.push({
    role: 'ai',
    content: '',
    time: new Date()
  })
  
  // 关闭之前的连接
  if (eventSource) {
    eventSource.close()
  }
  
  // 调用 SSE 接口
  eventSource = chatWithLifeAppSSE(
    userMessage,
    chatId.value,
    (data) => {
      // 实时更新 AI 消息
      if (messages.value[aiMessageIndex]) {
        messages.value[aiMessageIndex].content += data
      }
      scrollToBottom()
    },
    (error) => {
      console.error('SSE 错误:', error)
      isLoading.value = false
      if (messages.value[aiMessageIndex]) {
        messages.value[aiMessageIndex].content += '\n\n[连接错误，请重试]'
      }
    },
    () => {
      isLoading.value = false
      scrollToBottom()
    }
  )
}

// 格式化消息（支持换行）
const formatMessage = (text) => {
  if (!text) return ''
  return text.replace(/\n/g, '<br>')
}

// 格式化时间
const formatTime = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`
}

// 返回主页
const goBack = () => {
  if (eventSource) {
    eventSource.close()
  }
  router.push('/')
}

// 组件卸载时关闭连接
onUnmounted(() => {
  if (eventSource) {
    eventSource.close()
  }
})
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: radial-gradient(circle at 20% 20%, rgba(124, 93, 255, 0.15), transparent 25%),
    radial-gradient(circle at 80% 0%, rgba(25, 227, 177, 0.12), transparent 30%),
    #0c0f14;
  padding-bottom: 20px;
}

.chat-header {
  background: linear-gradient(135deg, rgba(124, 93, 255, 0.12), rgba(25, 227, 177, 0.08));
  color: var(--text);
  padding: 20px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--border);
  backdrop-filter: blur(6px);
}

.left {
  display: flex;
  align-items: center;
  gap: 14px;
}

.back-btn {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid var(--border);
  color: var(--text);
  padding: 8px 14px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.14);
  box-shadow: var(--glow);
}

.titles h2 {
  margin: 0;
  font-size: 20px;
  letter-spacing: 0.5px;
}

.subtitle {
  color: var(--text-muted);
  font-size: 13px;
}

.chat-id {
  font-size: 12px;
  color: var(--text-muted);
  background: var(--bg-muted);
  padding: 6px 10px;
  border-radius: 10px;
  border: 1px solid var(--border);
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.message {
  display: flex;
  gap: 10px;
  max-width: 80%;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-message {
  align-self: flex-end;
  margin-left: auto;
  flex-direction: row-reverse;
}

.ai-message {
  align-self: flex-start;
}

.avatar {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  display: grid;
  place-items: center;
  font-weight: 700;
  color: #fff;
  box-shadow: var(--glow);
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.message-content {
  padding: 12px 16px;
  border-radius: 16px;
  box-shadow: var(--shadow-soft);
  border: 1px solid var(--border);
}

.user-message .message-content {
  background: linear-gradient(135deg, rgba(124, 93, 255, 0.22), rgba(25, 227, 177, 0.18));
  color: #fff;
  border-bottom-right-radius: 6px;
  box-shadow: var(--glow);
}

.ai-message .message-content {
  background: var(--bg-card);
  color: var(--text);
  border-bottom-left-radius: 6px;
}

.message-text {
  line-height: 1.7;
  word-wrap: break-word;
  margin-bottom: 6px;
}

.message-time {
  font-size: 0.75rem;
  color: var(--text-muted);
  margin-top: 2px;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 5px 0;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #999;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.7;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

.chat-input-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 20px 24px;
  background: rgba(12, 15, 20, 0.7);
  border-top: 1px solid var(--border);
  position: sticky;
  bottom: 0;
  backdrop-filter: blur(10px);
}

.input-meta {
  display: flex;
  gap: 10px;
  align-items: center;
}

.pill {
  padding: 6px 12px;
  border-radius: 999px;
  border: 1px solid var(--border);
  background: rgba(124, 93, 255, 0.1);
  color: var(--text);
  font-size: 12px;
}

.pill.secondary {
  background: rgba(25, 227, 177, 0.08);
  color: #9cf6e3;
}

.chat-input {
  flex: 1;
  padding: 14px 16px;
  border: 1px solid var(--border);
  border-radius: 16px;
  font-size: 14px;
  resize: none;
  font-family: inherit;
  background: var(--bg-card);
  color: var(--text);
  min-height: 72px;
  line-height: 1.6;
}

.chat-input:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: var(--glow);
}

.chat-input:disabled {
  background: #0f1420;
  cursor: not-allowed;
}

.send-btn {
  align-self: flex-end;
  padding: 12px 26px;
  background: linear-gradient(135deg, #7c5dff 0%, #19e3b1 100%);
  color: white;
  border: none;
  border-radius: 14px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 700;
  transition: transform 0.2s, box-shadow 0.2s;
  white-space: nowrap;
  box-shadow: var(--glow);
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(124, 93, 255, 0.4);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .message {
    max-width: 90%;
  }
  
  .chat-header {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
}
</style>

