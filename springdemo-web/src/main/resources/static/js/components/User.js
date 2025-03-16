// 导入Vue的必要函数

export default {
    name: 'UserManagement',
    template: '#user-list',
    data() {
        return {
            users: [],
            loading: false,
            error: null,
            queryParams: {
                username: '',
                email: ''
            },
            pagination: {
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0
            }
        };
    },
    methods: {
        async fetchUsers() {
            console.log('Fetching users...');
            this.loading = true;
            try {
                const params = new URLSearchParams();
                if (this.queryParams.username) {
                    params.append('username', this.queryParams.username);
                }
                if (this.queryParams.email) {
                    params.append('email', this.queryParams.email);
                }
                params.append('page', this.pagination.page);
                params.append('size', this.pagination.size);

                const response = await fetch(`/springdemo/api/users?${params.toString()}`);
                if (!response.ok) {
                    throw new Error('获取用户数据失败');
                }
                const result = await response.json();
                if (result.code === 200) {
                    const pageData = result.data;
                    this.users = pageData.content;
                    this.pagination.totalElements = pageData.totalElements;
                    this.pagination.totalPages = pageData.totalPages;
                    this.pagination.page = pageData.number;
                    this.pagination.size = pageData.size;
                } else {
                    throw new Error(result.message || '获取用户数据失败');
                }
            } catch (err) {
                this.error = err.message;
                console.error('Error fetching users:', err);
            } finally {
                this.loading = false;
            }
        },
        handlePageChange(page) {
            this.pagination.page = page;
            this.fetchUsers();
        },
        handleSearch() {
            this.pagination.page = 0; // 重置到第一页
            this.fetchUsers();
        },
        resetSearch() {
            this.queryParams = {
                username: '',
                email: ''
            };
            this.pagination.page = 0; // 重置到第一页
            this.fetchUsers();
        },
        async handleImport() {
            try {
                // 创建文件选择器
                const input = document.createElement('input');
                input.type = 'file';
                input.accept = '.xlsx,.xls';
                input.style.display = 'none';
                document.body.appendChild(input);

                input.onchange = async e => {
                    const file = e.target.files[0];
                    if (file) {
                        const formData = new FormData();
                        formData.append('file', file);

                        const response = await fetch('/springdemo/api/users/import', {
                            method: 'POST',
                            body: formData
                        });

                        if (!response.ok) {
                            throw new Error('导入用户数据失败');
                        }

                        const result = await response.json();
                        if (result.code === 200) {
                            this.fetchUsers(); // 刷新用户列表
                        } else {
                            throw new Error(result.message || '导入用户数据失败');
                        }
                    }
                    document.body.removeChild(input);
                };

                input.click();
            } catch (err) {
                console.error('导入用户数据失败:', err);
                this.error = '导入用户数据失败: ' + err.message;
            }
        },
        async handleExport() {
            try {
                const response = await fetch('/springdemo/api/users/export');
                if (!response.ok) {
                    throw new Error('导出用户数据失败');
                }
                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'users.xlsx';
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
            } catch (err) {
                console.error('导出用户数据失败:', err);
                this.error = '导出用户数据失败: ' + err.message;
            }
        },
        async handleEdit(user) {
            // TODO: 实现编辑用户功能
            console.log('编辑用户:', user);
        },
        async handleToggleStatus(user) {
            try {
                const response = await fetch(`/springdemo/api/users/${user.id}/toggle-status`, {
                    method: 'PUT'
                });
                if (!response.ok) {
                    throw new Error('更新用户状态失败');
                }
                const result = await response.json();
                if (result.code === 200) {
                    this.fetchUsers(); // 刷新用户列表
                } else {
                    throw new Error(result.message || '更新用户状态失败');
                }
            } catch (err) {
                console.error('更新用户状态失败:', err);
                this.error = '更新用户状态失败: ' + err.message;
            }
        }
    },
    mounted() {
        this.fetchUsers();
    }
};
