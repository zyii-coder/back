<template>
  <el-card class="about">
    <h2 class="page-title">关于本项目</h2>
    <p>
      这是一个校园失物招领系统示例，采用 <b>Spring Boot</b> 提供后端 REST API，
      <b>Vue 3 + Vite</b> 构建前端单页应用，前后端分离、通过 JWT 鉴权。
    </p>
    <el-divider />
    <h3>技术栈</h3>
    <ul>
      <li>后端：Spring Boot 3.4 / Spring Data JPA / Spring Security / JWT (jjwt)</li>
      <li>数据库：开发用 H2（内存），生产可切换 MySQL（见 application.yml 的 mysql profile）</li>
      <li>前端：Vue 3.5 / Vite 5 / Vue Router / Pinia / Axios / Element Plus</li>
    </ul>
    <el-divider />
    <h3>目录</h3>
    <ul>
      <li><code>backend/</code> —— Spring Boot 工程（Maven，含 Maven Wrapper）</li>
      <li><code>frontend/</code> —— Vue 工程（npm）</li>
    </ul>
    <p class="tip">
      提示：开发时前端通过 Vite 代理将 <code>/api</code> 转发到 <code>http://localhost:8080</code>，
      无需额外配置跨域。
    </p>
  </el-card>
</template>

<style scoped>
.about {
  line-height: 1.8;
}
.about ul {
  padding-left: 20px;
}
.tip {
  color: #666;
  background: #f0f9ff;
  padding: 12px;
  border-radius: 8px;
}
code {
  background: #f4f4f5;
  padding: 1px 6px;
  border-radius: 4px;
}
</style>
