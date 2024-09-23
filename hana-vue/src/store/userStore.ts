// store/userStore.ts
import { defineStore } from 'pinia';
import axios from 'axios';

export const useUserStore = defineStore('user', {
    state: () => ({
        isLoggedIn: false,
        userInfo: null,
    }),
    actions: {
        async login(userData: { username: string, password: string }) {
            try {
                const response = await axios.post('/api/login', userData);
                this.userInfo = response.data;
                this.isLoggedIn = true;
            } catch (error) {
                console.error("Login failed:", error);
            }
        },
        logout() {
            this.isLoggedIn = false;
            this.userInfo = null;
        }
    }
});
