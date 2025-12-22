const gradientPool = [
  { bg: 'linear-gradient(135deg, #7c5dff 0%, #19e3b1 100%)', text: 'AI' },
  { bg: 'linear-gradient(135deg, #ff6bcb 0%, #845ec2 100%)', text: 'AI' },
  { bg: 'linear-gradient(135deg, #3c8ce7 0%, #00eaff 100%)', text: 'AI' },
  { bg: 'linear-gradient(135deg, #ff8c37 0%, #ff416c 100%)', text: 'AI' },
  { bg: 'linear-gradient(135deg, #1f1c2c 0%, #928dab 100%)', text: 'AI' }
]

const userGradientPool = [
  { bg: 'linear-gradient(135deg, #19e3b1 0%, #7c5dff 100%)', text: 'U1' },
  { bg: 'linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%)', text: 'U2' },
  { bg: 'linear-gradient(135deg, #00c6ff 0%, #0072ff 100%)', text: 'U3' },
  { bg: 'linear-gradient(135deg, #f7971e 0%, #ffd200 100%)', text: 'U4' },
  { bg: 'linear-gradient(135deg, #536976 0%, #292e49 100%)', text: 'U5' }
]

export const lifeAiAvatar = { bg: 'linear-gradient(135deg, #7c5dff 0%, #19e3b1 100%)', text: 'Life' }
export const manusAiAvatar = { bg: 'linear-gradient(135deg, #ff6bcb 0%, #845ec2 100%)', text: 'Bot' }

export function getRandomUserAvatar() {
  return userGradientPool[Math.floor(Math.random() * userGradientPool.length)]
}

export function getRandomAiAvatar() {
  return gradientPool[Math.floor(Math.random() * gradientPool.length)]
}

