<template>
  <AppHeader />
  <main class="container">
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </main>
  <footer class="footer">© 2026 失物招领系统 · Wuy University</footer>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue'
</script>

<style>
.container {
  max-width: 1100px;
  margin: 24px auto;
  padding: 0 16px;
}
.footer {
  text-align: center;
  color: #999;
  padding: 28px;
  font-size: 13px;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.18s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
