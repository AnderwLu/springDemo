// Dashboard component
export default {
    template: '#dashboard-template',
    props: ['systemInfo', 'breadcrumbs'],
    data() {
        return {
            userCount: 0,
            jobCount: 0,
            systemLoad: '0%',
            memoryUsage: '0%',
            recentJobs: []
        };
    },
    mounted() {
        this.fetchDashboardData();
        this.initCharts();
    },
    methods: {
        fetchDashboardData() {
            // 使用全局 $http 对象从 API 获取数据
            this.$http
                .get('/springdemo/api/admin/dashboard')
                .then(data => {
                    // 设置用户信息
                    this.userCount = data.userCount || 100;
                    this.jobCount = data.jobCount || 5;
                    this.systemLoad = data.systemLoad || '25%';
                    this.memoryUsage = data.memoryUsage || '40%';

                    // 获取最近任务
                    if (data.recentJobs && Array.isArray(data.recentJobs)) {
                        this.recentJobs = data.recentJobs;
                    }
                })
                .catch(error => {
                    console.error('获取仪表盘数据失败:', error.message);

                    // 设置默认数据
                    this.userCount = 100;
                    this.jobCount = 5;
                    this.systemLoad = '25%';
                    this.memoryUsage = '40%';
                });
        },
        initCharts() {
            // 用户统计图表
            const userCtx = document.getElementById('userChart').getContext('2d');
            const userChart = new Chart(userCtx, {
                type: 'bar',
                data: {
                    labels: ['1月', '2月', '3月', '4月', '5月', '6月'],
                    datasets: [
                        {
                            label: '新增用户',
                            data: [12, 19, 3, 5, 2, 3],
                            backgroundColor: 'rgba(58, 142, 230, 0.2)',
                            borderColor: 'rgba(58, 142, 230, 1)',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });

            // 系统状态图表
            const systemCtx = document.getElementById('systemChart').getContext('2d');
            const systemChart = new Chart(systemCtx, {
                type: 'line',
                data: {
                    labels: ['1小时前', '50分钟前', '40分钟前', '30分钟前', '20分钟前', '10分钟前', '现在'],
                    datasets: [
                        {
                            label: 'CPU使用率',
                            data: [30, 35, 40, 50, 45, 40, 35],
                            backgroundColor: 'rgba(58, 142, 230, 0.2)',
                            borderColor: 'rgba(58, 142, 230, 1)',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }
    }
};
