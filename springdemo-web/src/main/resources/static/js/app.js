// 导入路由模块
import router from './router.js';
import templateLoader from './TemplateLoad.js';

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
            // 从API获取系统信息
            axios
                .get('/springdemo/api/admin/dashboard')
                .then(response => {
                    this.systemInfo = response.data;
                })
                .catch(error => {
                    console.error('获取系统信息失败:', error);
                });
        }
    }
});

// 挂载应用
window.addEventListener('DOMContentLoaded', () => {
    app.mount('#app');
});

export default app;
