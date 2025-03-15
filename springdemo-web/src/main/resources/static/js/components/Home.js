// Home component
export default {
    template: '#home-template',
    props: ['systemInfo'],
    data() {
        return {
            recentActivities: []
        }
    },
    mounted() {
        // 模拟获取最近活动数据
        this.recentActivities = [
            {
                id: 1,
                title: '系统更新',
                description: '系统已更新到最新版本。',
                time: '2023-01-01 12:00:00'
            },
            {
                id: 2,
                title: '用户登录',
                description: '管理员登录了系统。',
                time: '2023-01-01 11:30:00'
            }
        ];
    }
}; 