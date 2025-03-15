/**
 * 仪表盘JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard loaded');
    initUserChart();
    initSystemChart();
    
    // 模拟获取实时数据
    setInterval(updateSystemStats, 5000);
});

/**
 * 初始化用户统计图表
 */
function initUserChart() {
    const ctx = document.getElementById('userChart').getContext('2d');
    
    // 模拟数据 - 实际应用中应从后端获取
    const months = ['1月', '2月', '3月', '4月', '5月', '6月'];
    const userData = [12, 19, 25, 32, 45, 60];
    
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: months,
            datasets: [{
                label: '用户增长',
                data: userData,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 2,
                tension: 0.3,
                fill: true
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: '用户增长趋势'
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

/**
 * 初始化系统状态图表
 */
function initSystemChart() {
    const ctx = document.getElementById('systemChart').getContext('2d');
    
    // 模拟数据 - 实际应用中应从后端获取
    const timeLabels = ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'];
    const cpuData = [30, 45, 65, 80, 60, 40];
    const memoryData = [50, 55, 60, 70, 65, 55];
    
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: timeLabels,
            datasets: [
                {
                    label: 'CPU使用率',
                    data: cpuData,
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 2,
                    tension: 0.2
                },
                {
                    label: '内存使用率',
                    data: memoryData,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 2,
                    tension: 0.2
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: '系统资源使用情况'
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    title: {
                        display: true,
                        text: '使用率 (%)'
                    }
                }
            }
        }
    });
}

/**
 * 更新系统统计数据
 */
function updateSystemStats() {
    // 模拟数据 - 实际应用中应从后端获取
    const cpuLoad = Math.floor(Math.random() * 100);
    const memoryUsage = Math.floor(Math.random() * 100);
    
    // 更新显示
    document.getElementById('cpuLoad').textContent = cpuLoad + '%';
    document.getElementById('memoryUsage').textContent = memoryUsage + '%';
    
    // 更新进度条
    document.getElementById('cpuProgress').style.width = cpuLoad + '%';
    document.getElementById('memoryProgress').style.width = memoryUsage + '%';
    
    // 根据负载调整颜色
    updateProgressColor('cpuProgress', cpuLoad);
    updateProgressColor('memoryProgress', memoryUsage);
}

/**
 * 根据负载调整进度条颜色
 */
function updateProgressColor(elementId, value) {
    const element = document.getElementById(elementId);
    
    // 移除所有可能的类
    element.classList.remove('bg-success', 'bg-warning', 'bg-danger');
    
    // 根据值添加适当的类
    if (value < 60) {
        element.classList.add('bg-success');
    } else if (value < 85) {
        element.classList.add('bg-warning');
    } else {
        element.classList.add('bg-danger');
    }
} 