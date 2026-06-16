import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import { useUserStore } from './stores/useUserStore'
import './styles/global.scss'

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
app.use(router)

// 初始化用户状态
const userStore = useUserStore()
userStore.fetchUser()

// 全局路由守卫（必须在 app.use(router) 之后、挂载之前设置）
router.beforeEach((to, _from, next) => {
    if (to.meta.guest && userStore.isLoggedIn) {
        next('/')
    } else {
        next()
    }
})

app.mount('#app')