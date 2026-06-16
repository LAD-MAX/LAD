import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  base: '/',   // 确保资源从根路径加载
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:3000',  // 开发时保留，方便本地调试
    },
  },
});