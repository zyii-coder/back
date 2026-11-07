import { defineStore } from 'pinia'
import { login as apiLogin, register as apiRegister } from '@/api/auth'

/**
 * Auth store: holds the JWT + minimal user info, persisted to localStorage
 * so a refresh keeps the session. The token is attached to every request
 * by the axios interceptor in @/api/http.
 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null')
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
    username: (state) => state.user?.username || '',
    role: (state) => state.user?.role || ''
  },
  actions: {
    async login(payload) {
      const res = await apiLogin(payload)
      this._setSession(res.data)
      return res.data
    },
    async register(payload) {
      const res = await apiRegister(payload)
      this._setSession(res.data)
      return res.data
    },
    _setSession(data) {
      this.token = data.token
      this.user = { username: data.username, role: data.role }
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(this.user))
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
