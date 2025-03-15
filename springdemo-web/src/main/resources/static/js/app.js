// 注册全局组件
const app = Vue.createApp({
  data() {
    return {
      currentPage: 'home'
    }
  },
  computed: {
    // 根据当前页面路径确定显示哪个组件
    currentComponent() {
      const path = window.location.pathname;
      
      if (path.includes('/batch/import')) {
        return 'batch-import';
      }
      
      return 'home';
    }
  }
});

// 注册组件
app.component('batch-import', BatchImport);

// 挂载应用
document.addEventListener('DOMContentLoaded', () => {
  app.mount('#app');
}); 