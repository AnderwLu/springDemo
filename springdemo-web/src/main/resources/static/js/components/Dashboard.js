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
        }
    },
    mounted() {
        this.fetchDashboardData();
        this.initCharts();
    },
    methods: {
        fetchDashboardData() {
            // 这里应该从API获取数据
            // 模拟数据
            this.userCount = 100;
            this.jobCount = 5;
            this.systemLoad = '25%';
            this.memoryUsage = '40%';
            this.recentJobs = [];
        },
        initCharts() {
            // 用户统计图表
            const userCtx = document.getElementById('userChart').getContext('2d');
            const userChart = new Chart(userCtx, {
                type: 'bar',
                data: {
                    labels: ['1月', '2月', '3月', '4月', '5月', '6月'],
                    datasets: [{
                        label: '新增用户',
                        data: [12, 19, 3, 5, 2, 3],
                        backgroundColor: 'rgba(58, 142, 230, 0.2)',
                        borderColor: 'rgba(58, 142, 230, 1)',
                        borderWidth: 1
                    }]
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
                    datasets: [{
                        label: 'CPU使用率',
                        data: [30, 35, 40, 50, 45, 40, 35],
                        backgroundColor: 'rgba(58, 142, 230, 0.2)',
                        borderColor: 'rgba(58, 142, 230, 1)',
                        borderWidth: 1
                    }]
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