<template>
  <section class="hero">
    <h1>校园失物招领平台</h1>
    <p>丢失的物品在这里被找到，捡到的东西在这里回到家。</p>
    <div class="hero__actions">
      <router-link to="/lost"><el-button type="danger" size="large">查看失物</el-button></router-link>
      <router-link to="/found"><el-button type="success" size="large">查看招领</el-button></router-link>
      <router-link to="/publish"><el-button type="primary" size="large">我要发布</el-button></router-link>
    </div>
  </section>

  <section class="recent">
    <h2 class="page-title">最新发布</h2>
    <div v-loading="loading" class="grid">
      <ItemCard v-for="it in items" :key="it.id" :item="it" />
      <el-empty v-if="!loading && items.length === 0" description="暂无记录" />
    </div>
    <div class="recent__more"><router-link to="/lost">查看更多 →</router-link></div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ItemCard from '@/components/ItemCard.vue'
import { searchItems } from '@/api/item'

const items = ref([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    const res = await searchItems({ page: 0, size: 8 })
    items.value = res.data.content
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.hero {
  text-align: center;
  padding: 48px 16px;
  background: #fff;
  border-radius: 12px;
}
.hero h1 {
  margin: 0 0 12px;
  font-size: 32px;
}
.hero p {
  color: #666;
  margin: 0 0 24px;
}
.hero__actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}
.recent {
  margin-top: 28px;
}
.recent__more {
  text-align: right;
  margin-top: 12px;
}
</style>
