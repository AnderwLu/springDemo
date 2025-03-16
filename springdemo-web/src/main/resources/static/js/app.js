// 导入路由模块
import router from './router.js';

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
        this.loadTemplates().then(() => {
            this.loading = false;
            this.handleRoute();
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
        },
        // 加载模板
        async loadTemplates() {
            try {
                // 加载主要模板
                const [dashboardResponse, homeResponse, settingsResponse, userListResponse] = await Promise.all([fetch('/springdemo/templates/dashboard.html'), fetch('/springdemo/templates/home.html'), fetch('/springdemo/templates/settings.html'), fetch('/springdemo/templates/user/list.html')]);

                // 创建模板元素
                const dashboardTemplate = document.createElement('script');
                dashboardTemplate.id = 'dashboard-template';
                dashboardTemplate.type = 'text/x-template';
                dashboardTemplate.innerHTML = await dashboardResponse.text();

                const homeTemplate = document.createElement('script');
                homeTemplate.id = 'home-template';
                homeTemplate.type = 'text/x-template';
                homeTemplate.innerHTML = await homeResponse.text();

                const settingsTemplate = document.createElement('script');
                settingsTemplate.id = 'settings-template';
                settingsTemplate.type = 'text/x-template';
                settingsTemplate.innerHTML = await settingsResponse.text();

                const userListTemplate = document.createElement('script');
                userListTemplate.id = 'user-list';
                userListTemplate.type = 'text/x-template';
                userListTemplate.innerHTML = await userListResponse.text();

                // 添加到DOM
                const templatesContainer = document.createElement('div');
                templatesContainer.id = 'templates';
                templatesContainer.style.display = 'none';

                templatesContainer.appendChild(dashboardTemplate);
                templatesContainer.appendChild(homeTemplate);
                templatesContainer.appendChild(settingsTemplate);
                templatesContainer.appendChild(userListTemplate);

                document.body.appendChild(templatesContainer);

                // 重新处理路由，确保组件正确加载
                this.handleRoute();
            } catch (error) {
                console.error('加载模板失败:', error);
            }
        }
    }
});

// 挂载应用
window.addEventListener('DOMContentLoaded', () => {
    app.mount('#app');
});

export default app;
