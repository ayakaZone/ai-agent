<template>
  <div class="chat-container">
    <div class="chat-header">
      <button class="back-btn" @click="goBack">← 返回</button>
      <h2>AI 超级智能体</h2>
    </div>
    <div class="chat-messages" ref="messagesContainer">
      <div
        v-for="(message, index) in messages"
        :key="index"
        :class="['message', message.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="message-content">
          <div class="message-text">{{ message.content }}</div>
          <div class="message-time">{{ message.time }}</div>
        </div>
      </div>
      <div v-if="isLoading" class="message ai-message">
        <div class="message-content">
          <div class="message-text typing">AI正在思考...</div>
        </div>
      </div>
    </div>
    <div class="chat-input-container">
      <input
        v-model="inputMessage"
        type="text"
        class="chat-input"
        placeholder="输入您的消息..."
        @keyup.enter="sendMessage"
        :disabled="isLoading"
      />
      <button class="send-btn" @click="sendMessage" :disabled="isLoading || !inputMessage.trim()">
        发送
      </button>
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { ref, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { createSSEConnection, closeSSEConnection } from '../utils/sse'
import Footer from '../components/Footer.vue'

const router = useRouter()
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)
let eventSource = null
let fullBuffer = '' // 完整的数据缓冲区
let stepMessagesMap = new Map() // 步骤编号 -> 消息索引的映射

// 格式化时间
const formatTime = () => {
  const now = new Date()
  return now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 发送消息
const sendMessage = () => {
  if (!inputMessage.value.trim() || isLoading.value) return

  const userMessage = {
    role: 'user',
    content: inputMessage.value.trim(),
    time: formatTime()
  }

  messages.value.push(userMessage)
  const messageToSend = inputMessage.value.trim()
  inputMessage.value = ''
  isLoading.value = true
  scrollToBottom()

  // 关闭之前的连接
  if (eventSource) {
    closeSSEConnection(eventSource)
    eventSource = null
  }

  // 重置步骤处理状态
  fullBuffer = ''
  stepMessagesMap.clear()

  // 建立SSE连接
  const url = 'http://localhost:8123/api/ai/manus/chat'
  const params = {
    message: messageToSend
  }

  eventSource = createSSEConnection(
    url,
    params,
    (data) => {
      // 将新数据添加到完整缓冲区
      fullBuffer += data
      
      // 检测所有步骤分隔符（Step 1:, Step 2: 等），不区分大小写
      const stepPattern = /Step\s+(\d+)\s*:/gi
      const allStepMatches = []
      let match
      
      // 重置正则表达式，从头开始匹配整个缓冲区
      stepPattern.lastIndex = 0
      while ((match = stepPattern.exec(fullBuffer)) !== null) {
        const stepNumber = parseInt(match[1])
        allStepMatches.push({
          index: match.index,
          stepSeparator: match[0],
          stepNumber: stepNumber,
          contentStart: match.index + match[0].length
        })
      }
      
      // 处理每个步骤
      for (let i = 0; i < allStepMatches.length; i++) {
        const currentStep = allStepMatches[i]
        const nextStep = allStepMatches[i + 1]
        
        // 确定步骤内容的结束位置
        const stepEndIndex = nextStep ? nextStep.index : fullBuffer.length
        const stepContent = fullBuffer.substring(currentStep.contentStart, stepEndIndex)
        const fullStepText = currentStep.stepSeparator.trim() + stepContent
        
        // 检查这个步骤是否已经创建了消息
        if (!stepMessagesMap.has(currentStep.stepNumber)) {
          // 新步骤，创建新消息
          const stepMessage = {
            role: 'ai',
            content: fullStepText,
            time: formatTime()
          }
          messages.value.push(stepMessage)
          stepMessagesMap.set(currentStep.stepNumber, messages.value.length - 1)
        } else {
          // 已存在的步骤，更新消息内容
          const messageIndex = stepMessagesMap.get(currentStep.stepNumber)
          if (messageIndex >= 0 && messageIndex < messages.value.length) {
            messages.value[messageIndex].content = fullStepText
          }
        }
      }
      
      scrollToBottom()
    },
    (error) => {
      console.error('SSE error:', error)
      isLoading.value = false
      // 关闭连接
      if (eventSource) {
        closeSSEConnection(eventSource)
        eventSource = null
      }
      if (messages.value.length > 0) {
        const lastMessage = messages.value[messages.value.length - 1]
        if (lastMessage.role === 'ai' && !lastMessage.content) {
          lastMessage.content = '抱歉，发生了错误，请重试。'
        }
      }
    },
    () => {
      // 流正常结束，所有步骤内容已经在处理过程中更新完成
      // 重置状态
      fullBuffer = ''
      stepMessagesMap.clear()
      isLoading.value = false
      eventSource = null
      scrollToBottom()
    }
  )
}

// 返回主页
const goBack = () => {
  if (eventSource) {
    closeSSEConnection(eventSource)
  }
  router.push('/')
}

// 组件卸载时关闭连接
onUnmounted(() => {
  if (eventSource) {
    closeSSEConnection(eventSource)
  }
})
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(135deg, #0d0d0d 0%, #1a1a1a 100%);
}

.chat-header {
  background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
  border-bottom: 1px solid #333;
  color: #fff;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.5);
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-btn {
  background: rgba(0, 255, 136, 0.1);
  border: 1px solid rgba(0, 255, 136, 0.3);
  color: #00ff88;
  padding: 8px 15px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  font-family: 'Consolas', 'Monaco', monospace;
}

.back-btn:hover {
  background: rgba(0, 255, 136, 0.2);
  border-color: #00ff88;
  box-shadow: 0 0 10px rgba(0, 255, 136, 0.3);
}

.chat-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  background: linear-gradient(135deg, #00ff88 0%, #00d4ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  background: #0d0d0d;
}

.message {
  display: flex;
  max-width: 70%;
  animation: fadeIn 0.3s;
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
  justify-content: flex-end;
}

.ai-message {
  align-self: flex-start;
  justify-content: flex-start;
}

.message-content {
  padding: 12px 16px;
  border-radius: 18px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-message .message-content {
  background: linear-gradient(135deg, #00ff88 0%, #00cc6a 100%);
  color: #0d0d0d;
  border-bottom-right-radius: 4px;
  border: 1px solid rgba(0, 255, 136, 0.3);
  box-shadow: 0 2px 10px rgba(0, 255, 136, 0.2);
}

.ai-message .message-content {
  background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
  color: #e0e0e0;
  border: 1px solid #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.message-text {
  font-size: 15px;
  line-height: 1.5;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.message-time {
  font-size: 11px;
  opacity: 0.7;
  margin-top: 5px;
}

.user-message .message-time {
  text-align: right;
}

.typing {
  display: inline-block;
}

.typing::after {
  content: '...';
  animation: dots 1.5s steps(4, end) infinite;
}

@keyframes dots {
  0%, 20% {
    content: '.';
  }
  40% {
    content: '..';
  }
  60%, 100% {
    content: '...';
  }
}

.chat-input-container {
  padding: 20px;
  background: linear-gradient(135deg, #1a1a1a 0%, #252525 100%);
  border-top: 1px solid #333;
  display: flex;
  gap: 10px;
  position: sticky;
  bottom: 0;
  z-index: 100;
}

.chat-input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #333;
  border-radius: 24px;
  font-size: 15px;
  outline: none;
  transition: all 0.3s;
  background: #0d0d0d;
  color: #e0e0e0;
  font-family: inherit;
}

.chat-input::placeholder {
  color: #666;
}

.chat-input:focus {
  border-color: #00ff88;
  box-shadow: 0 0 10px rgba(0, 255, 136, 0.3);
}

.chat-input:disabled {
  background-color: #1a1a1a;
  cursor: not-allowed;
  opacity: 0.5;
}

.send-btn {
  padding: 12px 30px;
  background: linear-gradient(135deg, #00ff88 0%, #00d4ff 100%);
  color: #0d0d0d;
  border: none;
  border-radius: 24px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  font-family: 'Consolas', 'Monaco', monospace;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 255, 136, 0.4);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

@media (max-width: 768px) {
  .message {
    max-width: 85%;
  }
  
  .chat-header h2 {
    font-size: 1.2rem;
  }
}
</style>


