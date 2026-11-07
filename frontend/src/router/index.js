import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const routes = [
  { path: '/', name: 'home', component: () => import('@/views/Home.vue') },
  { path: '/lost', name: 'lost', component: () => import('@/views/LostItems.vue') },
  { path: '/found', name: 'found', component: () => import('@/views/FoundItems.vue') },
  { path: '/publish', name: 'publish', component: () => import('@/views/Publish.vue'), meta: { requiresAuth: true } },
  { path: '/items/:id', name: 'item-detail', component: () => import('@/views/ItemDetail.vue') },
  { path: '/login', name: 'login', component: () => import('@/views/Login.vue') },
  { path: '/register', name: 'register', component: () => import('@/views/Register.vue') },
  { path: '/about', name: 'about', component: () => import('@/views/About.vue') },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Route guard: pages marked requiresAuth bounce unauthenticated users to /login.
router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
  return true
})

export default router
