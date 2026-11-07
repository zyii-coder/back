<template>
  <div>
    <h2 class="page-title">招领启事</h2>
    <el-card class="filter-bar">
      <el-input
        v-model="keyword"
        placeholder="搜索标题/描述/地点"
        clearable
        style="max-width: 260px"
        @keyup.enter="load"
      />
      <el-select v-model="category" placeholder="全部分类" clearable style="width: 150px">
        <el-option v-for="c in categories" :key="c.value" :label="c.label" :value="c.value" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </el-card>

    <div v-loading="loading" class="grid">
      <ItemCard v-for="it in items" :key="it.id" :item="it" />
    </div>
    <el-empty v-if="!loading && items.length === 0" description="没有匹配的招领记录" />

    <div class="pager" v-if="total > pageSize">
      <el-pagination
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="page"
        @current-change="onPage"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import ItemCard from '@/components/ItemCard.vue'
import { searchItems } from '@/api/item'
import { categories } from '@/constants'

const items = ref([])
const loading = ref(false)
const keyword = ref('')
const category = ref('')
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)

async function load() {
  loading.value = true
  try {
    const res = await searchItems({
      type: 'FOUND',
      keyword: keyword.value || undefined,
      category: category.value || undefined,
      page: page.value - 1,
      size: pageSize.value
    })
    items.value = res.data.content
    total.value = res.data.totalElements
  } catch (e) {
    ElMessage.error(e.userMessage || '加载失败')
  } finally {
    loading.value = false
  }
}

function reset() {
  keyword.value = ''
  category.value = ''
  page.value = 1
  load()
}

function onPage(p) {
  page.value = p
  load()
}

onMounted(load)
</script>

<style scoped>
.pager {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
