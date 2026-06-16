import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import request from '../api/request';

interface User {
    id: number;
    username: string;
    email: string;
    role: string;
    avatar?: string;
}

export const useUserStore = defineStore('user', () => {
    const user = ref<User | null>(null);
    const token = ref<string>(localStorage.getItem('token') || '');

    const isLoggedIn = computed(() => !!token.value);

    // 初始化时尝试获取用户信息
    const fetchUser = async () => {
        if (!token.value) return;
        try {
            const res = await request.get('/auth/me');
            if (res.data.success) {
                user.value = res.data.data;
            } else {
                // token 无效
                logout();
            }
        } catch (err) {
            logout();
        }
    };

    const login = async (username: string, password: string) => {
        const res = await request.post('/auth/login', { username, password });
        if (res.data.success) {
            token.value = res.data.data.token;
            user.value = res.data.data.user;
            localStorage.setItem('token', token.value);
            return true;
        }
        return false;
    };

    const register = async (username: string, email: string, password: string) => {
        const res = await request.post('/auth/register', { username, email, password });
        if (res.data.success) {
            token.value = res.data.data.token;
            user.value = res.data.data.user;
            localStorage.setItem('token', token.value);
            return true;
        }
        return false;
    };

    const logout = () => {
        token.value = '';
        user.value = null;
        localStorage.removeItem('token');
    };

    return { user, token, isLoggedIn, fetchUser, login, register, logout };
});