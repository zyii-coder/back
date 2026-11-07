<template>
  <div v-loading="loading" class="detail">
    <el-button text @click="$router.back()">← 返回</el-button>

    <el-card v-if="item" class="detail-card">
      <div class="detail-card__head">
        <div class="tags">
          <el-tag :type="item.type === 'LOST' ? 'danger' : 'success'" size="small">
            {{ typeLabel(item.type) }}
          </el-tag>
          <el-tag size="small" effect="plain">{{ categoryLabel(item.category) }}</el-tag>
          <el-tag v-if="item.status === 'CLAIMED'" size="small" type="info">已认领</el-tag>
          <el-tag v-else-if="item.status === 'CLOSED'" size="small" type="warning">已关闭</el-tag>
        </div>
        <el-button v-if="isOwner" size="small" type="danger" @click="onDelete">删除</el-button>
      </div>

      <h1 class="detail-card__title">{{ item.title }}</h1>
      <p class="detail-card__meta">
        发布人：{{ item.publisherUsername }} · {{ formatDate(item.createdAt) }} · 状态：{{ statusLabel(item.status) }}
      </p>

      <el-divider />

      <img v-if="item.imageUrl" :src="item.imageUrl" class="detail-card__img" alt="item" />
      <p class="detail-card__desc">{{ item.description || '暂无描述' }}</p>

      <ul class="detail-card__info">
        <li v-if="item.location"><b>地点：</b>{{ item.location }}</li>
        <li v-if="item.contactInfo"><b>联系方式：</b>{{ item.contactInfo }}</li>
      </ul>
    </el-card>

    <el-empty v-else-if="!loading" description="记录不存在" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getItem, deleteItem } from '@/api/item'
import { useAuthStore } from '@/store/auth'
import { categoryLabel, typeLabel, statusLabel } from '@/constants'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const item = ref(null)
const loading = ref(false)

const isOwner = computed(
  () => item.value && auth.username === item.value.publisherUsername
)

async function load() {
  loading.value = true
  try {
    const res = await getItem(route.params.id)
    item.value = res.data
  } catch (e) {
    ElMessage.error(e.userMessage || '加载失败')
  } finally {
    loading.value = false
  }
}

async function onDelete() {
  try {
    await ElMessageBox.confirm('确定删除这条记录吗？', '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    await deleteItem(item.value.id)
    ElMessage.success('已删除')
    router.push('/')
  } catch (e) {
    ElMessage.error(e.userMessage || '删除失败')
  }
}

function formatDate(d) {
  return d ? new Date(d).toLocaleString('zh-CN') : ''
}

onMounted(load)
</script>

<style scoped>
.detail-card {
  margin-top: 12px;
}
.detail-card__head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.detail-card__title {
  margin: 12px 0 6px;
  font-size: 24px;
}
.detail-card__meta {
  color: #999;
  font-size: 13px;
  margin: 0;
}
.detail-card__img {
  max-width: 100%;
  border-radius: 8px;
  margin-bottom: 12px;
}
.detail-card__desc {
  line-height: 1.7;
  white-space: pre-wrap;
}
.detail-card__info {
  list-style: none;
  padding: 0;
  color: #555;
}
.detail-card__info li {
  margin: 6px 0;
}
</style>
