import axios from 'axios'

// All calls use a relative baseURL. In dev, Vite proxies /api -> :8080.
// In prod, serve the built assets behind the same origin as the API (or set this to absolute).
const http = axios.create({
  baseURL: '/api',
  timeout: 15000
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

http.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status
    if (status === 401) {
      // Token missing/expired: clear session and send to login (avoid loops on login page).
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      if (location.pathname !== '/login') {
        location.href = '/login'
      }
    }
    // Normalize error message for UI consumption.
    const message =
      error.response?.data?.message ||
      (error.response?.data?.fields ? JSON.stringify(error.response.data.fields) : null) ||
      error.message
    error.userMessage = message
    return Promise.reject(error)
  }
)

export default http
