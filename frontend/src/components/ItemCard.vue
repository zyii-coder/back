<template>
  <el-card class="item-card" shadow="hover" @click="$router.push(`/items/${item.id}`)">
    <div class="item-card__top">
      <el-tag :type="item.type === 'LOST' ? 'danger' : 'success'" size="small">
        {{ typeLabel(item.type) }}
      </el-tag>
      <el-tag size="small" effect="plain">{{ categoryLabel(item.category) }}</el-tag>
      <el-tag v-if="item.status === 'CLAIMED'" size="small" type="info">已认领</el-tag>
    </div>
    <h3 class="item-card__title">{{ item.title }}</h3>
    <p class="item-card__desc">{{ item.description || '暂无描述' }}</p>
    <div class="item-card__meta">
      <span v-if="item.location">📍 {{ item.location }}</span>
      <span>{{ formatDate(item.createdAt) }}</span>
    </div>
  </el-card>
</template>

<script setup>
import { categoryLabel, typeLabel } from '@/constants'

const props = defineProps({
  item: { type: Object, required: true }
})

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.item-card {
  cursor: pointer;
}
.item-card__top {
  display: flex;
  gap: 6px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}
.item-card__title {
  margin: 0 0 6px;
  font-size: 16px;
}
.item-card__desc {
  color: #666;
  font-size: 13px;
  margin: 0 0 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.item-card__meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 12px;
}
</style>
