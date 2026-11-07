<template>
  <div class="publish">
    <h2 class="page-title">发布启事</h2>
    <el-card>
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="90px"
        style="max-width: 560px"
      >
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio value="LOST">我丢了东西</el-radio>
            <el-radio value="FOUND">我捡到东西</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="一句话描述，如：图书馆丢失黑色雨伞" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择" style="width: 100%">
            <el-option v-for="c in categories" :key="c.value" :label="c.label" :value="c.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-input v-model="form.location" placeholder="大概丢失 / 捡到地点" />
        </el-form-item>
        <el-form-item label="联系方式" prop="contactInfo">
          <el-input v-model="form.contactInfo" placeholder="手机号 / 微信 / 邮箱" />
        </el-form-item>
        <el-form-item label="图片链接" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="可选，图片 URL" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="补充细节，提高找回概率" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="onSubmit">提交</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createItem } from '@/api/item'
import { categories } from '@/constants'

const router = useRouter()
const formRef = ref()
const submitting = ref(false)

const form = reactive({
  type: 'LOST',
  title: '',
  category: '',
  location: '',
  contactInfo: '',
  imageUrl: '',
  description: ''
})

const rules = {
  type: [{ required: true }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const res = await createItem({ ...form })
    ElMessage.success('发布成功')
    router.push(`/items/${res.data.id}`)
  } catch (e) {
    ElMessage.error(e.userMessage || '发布失败')
  } finally {
    submitting.value = false
  }
}

function reset() {
  formRef.value?.resetFields()
}
</script>
