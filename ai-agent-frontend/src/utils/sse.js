/**
 * 创建SSE连接
 * @param {string} url - SSE接口URL
 * @param {Object} params - 请求参数
 * @param {Function} onMessage - 消息回调函数
 * @param {Function} onError - 错误回调函数
 * @param {Function} onComplete - 完成回调函数（流结束时调用）
 * @returns {EventSource} EventSource实例
 */
export function createSSEConnection(url, params, onMessage, onError, onComplete) {
  // 构建查询字符串
  const queryString = new URLSearchParams(params).toString()
  const fullUrl = `${url}?${queryString}`

  const eventSource = new EventSource(fullUrl)

  eventSource.onmessage = (event) => {
    if (onMessage) {
      onMessage(event.data)
    }
  }

  eventSource.onerror = (error) => {
    // 当readyState为CLOSED时，表示连接已关闭（正常结束）
    if (eventSource.readyState === EventSource.CLOSED) {
      // 正常关闭，调用完成回调
      if (onComplete) {
        onComplete()
      }
    } else {
      // 其他错误（如网络错误），调用错误回调
      if (onError) {
        onError(error)
      }
    }
  }

  return eventSource
}

/**
 * 关闭SSE连接
 * @param {EventSource} eventSource - EventSource实例
 */
export function closeSSEConnection(eventSource) {
  if (eventSource) {
    eventSource.close()
  }
}

/**
 * 生成唯一的聊天室ID
 * @returns {string} 聊天室ID
 */
export function generateChatId() {
  return `chat_${Date.now()}_${Math.random().toString(36).substring(2, 11)}`
}

