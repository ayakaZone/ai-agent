import axios from 'axios'

const API_BASE_URL = 'http://localhost:8123/api'

// 创建 axios 实例
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000
})

/**
 * SSE 流式请求 - AI 生活对谈导师
 * @param {string} message - 用户消息
 * @param {string} chatId - 聊天室 ID
 * @param {Function} onMessage - 消息回调函数
 * @param {Function} onError - 错误回调函数
 * @param {Function} onComplete - 完成回调函数
 */
export function chatWithLifeAppSSE(message, chatId, onMessage, onError, onComplete) {
  const url = `${API_BASE_URL}/ai/life_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`
  
  const eventSource = new EventSource(url)
  let hasReceivedData = false
  let isHandled = false
  
  // 关闭连接并防止重连的辅助函数
  const closeConnection = () => {
    if (!isHandled) {
      isHandled = true
      // 移除所有事件监听器，防止重连
      eventSource.onerror = null
      eventSource.onmessage = null
      eventSource.close()
    }
  }
  
  eventSource.onmessage = (event) => {
    if (event.data) {
      hasReceivedData = true
      // 检查是否是结束标记（空数据或特定标记）
      if (event.data.trim() === '' || event.data === '[DONE]' || event.data === '[END]') {
        closeConnection()
        if (onComplete) onComplete()
        return
      }
      onMessage(event.data)
    }
  }
  
  eventSource.onerror = (error) => {
    // EventSource 在连接关闭时会触发 onerror
    // 一旦触发 onerror，无论什么状态，都应该关闭连接防止重连
    if (!isHandled) {
      closeConnection()
      // 如果已经接收到数据，说明是正常完成
      if (hasReceivedData) {
        if (onComplete) onComplete()
      } else {
        // 没有接收到数据，可能是真正的错误
        onError(error)
      }
    }
  }
  
  // 监听完成事件（如果后端发送了结束标记）
  eventSource.addEventListener('complete', () => {
    closeConnection()
    if (onComplete) onComplete()
  })
  
  return eventSource
}

/**
 * SSE 流式请求 - AI 超级智能体
 * @param {string} message - 用户消息
 * @param {Function} onMessage - 消息回调函数
 * @param {Function} onError - 错误回调函数
 * @param {Function} onComplete - 完成回调函数
 */
export function chatWithManusSSE(message, onMessage, onError, onComplete) {
  const url = `${API_BASE_URL}/ai/manus/chat?message=${encodeURIComponent(message)}`
  
  const eventSource = new EventSource(url)
  let hasReceivedData = false
  let isHandled = false
  
  // 关闭连接并防止重连的辅助函数
  const closeConnection = () => {
    if (!isHandled) {
      isHandled = true
      // 移除所有事件监听器，防止重连
      eventSource.onerror = null
      eventSource.onmessage = null
      eventSource.close()
    }
  }
  
  eventSource.onmessage = (event) => {
    if (event.data) {
      hasReceivedData = true
      // 检查是否是结束标记（空数据或特定标记）
      if (event.data.trim() === '' || event.data === '[DONE]' || event.data === '[END]') {
        closeConnection()
        if (onComplete) onComplete()
        return
      }
      onMessage(event.data)
    }
  }
  
  eventSource.onerror = (error) => {
    // EventSource 在连接关闭时会触发 onerror
    // 一旦触发 onerror，无论什么状态，都应该关闭连接防止重连
    if (!isHandled) {
      closeConnection()
      // 如果已经接收到数据，说明是正常完成
      if (hasReceivedData) {
        if (onComplete) onComplete()
      } else {
        // 没有接收到数据，可能是真正的错误
        onError(error)
      }
    }
  }
  
  // 监听完成事件
  eventSource.addEventListener('complete', () => {
    closeConnection()
    if (onComplete) onComplete()
  })
  
  return eventSource
}

export default api

