// Home component
export default {
    template: '#home-template',
    props: ['systemInfo', 'breadcrumbs'],
    data() {
        return {
            welcomeMessage: '欢迎使用Spring Demo管理系统',
            announcements: []
        };
    },
    mounted() {
        this.fetchHomeData();
    },
    methods: {
        fetchHomeData() {
            // 使用全局 $http 对象从 API 获取数据
            this.$http
                .get('/springdemo/api/admin/index')
                .then(data => {
                    // 如果有欢迎消息，则更新
                    if (data.welcomeMessage) {
                        this.welcomeMessage = data.welcomeMessage;
                    }

                    // 如果有公告，则更新
                    if (data.announcements && Array.isArray(data.announcements)) {
                        this.announcements = data.announcements;
                    } else {
                        // 默认公告
                        this.announcements = [
                            { title: '系统公告', content: '系统已完成更新', date: '2023-03-15' },
                            { title: '欢迎使用', content: '感谢您使用Spring Demo', date: '2023-03-10' }
                        ];
                    }
                })
                .catch(error => {
                    console.error('获取首页数据失败:', error.message);

                    // 设置默认数据
                    this.announcements = [
                        { title: '系统公告', content: '系统已完成更新', date: '2023-03-15' },
                        { title: '欢迎使用', content: '感谢您使用Spring Demo', date: '2023-03-10' }
                    ];
                });
        }
    }
};
