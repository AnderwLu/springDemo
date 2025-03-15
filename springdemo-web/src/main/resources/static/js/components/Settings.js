// Settings component
export default {
    template: '#settings-template',
    data() {
        return {
            settings: {
                siteName: 'SpringDemo管理系统',
                siteDescription: '这是一个Spring Boot与Vue集成的演示项目',
                adminEmail: 'admin@example.com'
            }
        }
    },
    methods: {
        saveSettings() {
            // 模拟保存设置
            alert('设置已保存');
        }
    }
}; 