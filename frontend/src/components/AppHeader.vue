<template>
  <header class="app-header">
    <div class="app-header__inner">
      <router-link to="/" class="brand">失物招领系统</router-link>
      <nav>
        <router-link to="/lost">失物</router-link>
        <router-link to="/found">招领</router-link>
        <router-link to="/publish">发布</router-link>
        <router-link to="/about">关于</router-link>
      </nav>
      <div class="auth">
        <template v-if="auth.isAuthenticated">
          <span class="user">你好，{{ auth.username }}</span>
          <el-button text @click="onLogout">退出</el-button>
        </template>
        <template v-else>
          <router-link to="/login"><el-button text>登录</el-button></router-link>
          <router-link to="/register">
            <el-button type="primary" size="small">注册</el-button>
          </router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useAuthStore } from '@/store/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function onLogout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.app-header {
  background: #fff;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 10;
}
.app-header__inner {
  max-width: 1100px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 12px 16px;
}
.brand {
  font-weight: 700;
  font-size: 18px;
  color: var(--brand);
  text-decoration: none;
}
nav {
  display: flex;
  gap: 16px;
}
nav a {
  color: #333;
  text-decoration: none;
}
nav a.router-link-active {
  color: var(--brand);
  font-weight: 600;
}
.auth {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 8px;
}
.user {
  color: #666;
  font-size: 14px;
}
</style>
