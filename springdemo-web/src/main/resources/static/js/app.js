// 导入路由模块
import router from './router.js';
import templateLoader from './TemplateLoad.js';

// 添加 axios 请求拦截器，添加一些调试信息
axios.interceptors.request.use(
    config => {
        console.log('Request URL:', config.url);
        console.log('Request Method:', config.method);
        console.log('Request Params:', config.params || config.data);
        return config;
    },
    error => {
        console.error('Request error:', error);
        return Promise.reject(error);
    }
);

// 配置 axios 拦截器统一处理 Result 对象
axios.interceptors.response.use(
    response => {
        console.log('收到响应:', response.config.url);

        // 处理响应数据，提取 Result 中的 data 字段
        const result = response.data;
        console.log('原始响应数据结构:', result);

        // 检查是否是 Result 格式
        if (result && typeof result === 'object' && 'code' in result) {
            console.log('检测到 Result 格式数据');

            if (result.code === 200) {
                // 请求成功，返回 data 字段
                console.log('请求成功，返回数据内容:', result.data);
                return result.data;
            } else {
                // 请求失败，抛出异常
                console.error('API错误:', result.code, result.message);
                return Promise.reject(new Error(result.message || '请求失败'));
            }
        }

        // 如果不是 Result 格式，直接返回原始数据
        console.log('非 Result 格式，返回原始数据');
        return response.data;
    },
    error => {
        console.error('请求失败:', error.message);
        return Promise.reject(error);
    }
);

// 创建全局 HTTP 请求方法
const http = {
    get: (url, params) => axios.get(url, { params }),
    post: (url, data) => axios.post(url, data),
    put: (url, data) => axios.put(url, data),
    delete: url => axios.delete(url)
};

// 创建Vue应用
const app = Vue.createApp({
    data() {
        return {
            active: 'home',
            subActive: '',
            username: 'admin',
            breadcrumbs: [],
            systemInfo: {
                osName: 'Mac OS X',
                javaVersion: '1.8.0_361',
                serverTime: new Date().toLocaleString()
            },
            currentView: null,
            loading: true // 添加加载状态
        };
    },
    mounted() {
        // 初始化路由
        templateLoader
            .loadTemplates()
            .then(() => {
                this.loading = false;
                this.handleRoute();
            })
            .catch(error => {
                console.error('加载模板失败:', error);
                this.loading = false;
            });

        window.addEventListener('hashchange', this.handleRoute);

        // 获取系统信息
        this.fetchSystemInfo();
    },
    methods: {
        toggleSubmenu(menu) {
            if (this.active === menu) {
                this.active = '';
            } else {
                this.active = menu;
            }
        },
        handleRoute() {
            const hash = window.location.hash || '#/';
            const path = hash.substring(1);

            // 获取路由信息
            const { active, subActive } = router.getActiveMenu(path);
            this.active = active;
            this.subActive = subActive;

            // 获取面包屑
            this.breadcrumbs = router.getBreadcrumbs(path);

            // 加载组件
            this.currentView = router.getComponent(path);
        },
        fetchSystemInfo() {
            // 从API获取系统信息，使用拦截器处理返回的 Result 对象
            http.get('/springdemo/api/admin/dashboard')
                .then(data => {
                    // 由于拦截器已经提取了 data 字段，这里直接使用返回的数据
                    this.systemInfo = data;
                })
                .catch(error => {
                    console.error('获取系统信息失败:', error.message);
                });
        }
    }
});

// 将 http 对象挂载到全局，供所有组件使用
app.config.globalProperties.$http = http;

// 挂载应用
window.addEventListener('DOMContentLoaded', () => {
    app.mount('#app');
});

export default app;
