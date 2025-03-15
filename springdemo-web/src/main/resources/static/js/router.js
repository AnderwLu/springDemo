// router.js
import Home from './components/Home.js';
import Dashboard from './components/Dashboard.js';
import Settings from './components/Settings.js';
const { markRaw } = Vue; 
// 简单的路由实现
export default {
    routes: {
        '/': markRaw(Home),
        '/dashboard': markRaw(Dashboard),
        '/settings': markRaw(Settings)
    },
    
    // 根据路径返回组件
    getComponent(path) {
        path = path || '/';
        return this.routes[path] || this.routes['/'];
    },
    
    // 根据路径获取面包屑
    getBreadcrumbs(path) {
        path = path || '/';
        
        let breadcrumbs = [];
        
        if (path === '/') {
            breadcrumbs = [
                { name: '首页', url: '/', active: true }
            ];
        } else if (path === '/dashboard') {
            breadcrumbs = [
                { name: '仪表盘', url: '/dashboard', active: true }
            ];
        } else if (path === '/settings') {
            breadcrumbs = [
                { name: '系统设置', url: '/settings', active: true }
            ];
        }
        
        return breadcrumbs;
    },
    
    // 获取当前激活的菜单
    getActiveMenu(path) {
        path = path || '/';
        
        let active = '';
        let subActive = '';
        
        if (path === '/') {
            active = 'home';
        } else if (path === '/dashboard') {
            active = 'dashboard';
        } else if (path === '/settings') {
            active = 'settings';
        } else if (path.startsWith('/monitor')) {
            active = 'monitor';
            
            // 子菜单激活状态
            if (path === '/monitor/realtime') {
                subActive = 'realtime';
            } else if (path === '/monitor/logs') {
                subActive = 'logs';
            } else if (path === '/monitor/metrics') {
                subActive = 'metrics';
            }
        }
        
        return { active, subActive };
    }
}; 