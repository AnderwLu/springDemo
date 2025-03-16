import template from './template.js';
import styles from './styles.js';

const { defineComponent } = Vue;

export default defineComponent({
    name: 'UserManagement',
    template,
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
            },
            editingUser: null,
            showEditModal: false,
            newUser: {
                username: '',
                realName: '',
                email: '',
                phone: '',
                gender: '男',
                age: null,
                address: '',
                password: '',
                role: 'ROLE_USER',
                enabled: true
            },
            showAddModal: false
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
            try {
                // 克隆用户对象，避免直接修改列表中的数据
                this.editingUser = { ...user };
                this.showEditModal = true;
            } catch (err) {
                console.error('编辑用户失败:', err);
                this.error = '编辑用户失败: ' + err.message;
            }
        },
        async handleDelete(user) {
            try {
                if (!confirm('确定要删除该用户吗？')) {
                    return;
                }

                const response = await fetch(`/springdemo/api/users/${user.id}`, {
                    method: 'DELETE'
                });

                if (!response.ok) {
                    throw new Error('删除用户失败');
                }

                const result = await response.json();
                if (result.code === 200) {
                    // 删除成功后刷新列表
                    this.fetchUsers();
                } else {
                    throw new Error(result.message || '删除用户失败');
                }
            } catch (err) {
                console.error('删除用户失败:', err);
                this.error = '删除用户失败: ' + err.message;
            }
        },
        async handleSaveEdit() {
            try {
                if (!this.editingUser) return;

                if (!this.validateUser(this.editingUser)) {
                    return;
                }

                const response = await fetch(`/springdemo/api/users/${this.editingUser.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(this.editingUser)
                });

                if (!response.ok) {
                    throw new Error('更新用户失败');
                }

                const result = await response.json();
                if (result.code === 200) {
                    this.showEditModal = false;
                    this.editingUser = null;
                    this.fetchUsers();
                } else {
                    throw new Error(result.message || '更新用户失败');
                }
            } catch (err) {
                console.error('更新用户失败:', err);
                this.error = '更新用户失败: ' + err.message;
            }
        },
        handleCancelEdit() {
            this.showEditModal = false;
            this.editingUser = null;
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
        },
        handleAdd() {
            this.newUser = {
                username: '',
                realName: '',
                email: '',
                phone: '',
                gender: '男',
                age: null,
                address: '',
                password: '',
                role: 'ROLE_USER',
                enabled: true
            };
            this.showAddModal = true;
        },
        handleCancelAdd() {
            this.showAddModal = false;
            this.newUser = {
                username: '',
                realName: '',
                email: '',
                phone: '',
                gender: '男',
                age: null,
                address: '',
                password: '',
                role: 'ROLE_USER',
                enabled: true
            };
        },
        async handleSaveAdd() {
            try {
                if (!this.validateUser(this.newUser)) {
                    return;
                }

                const response = await fetch('/springdemo/api/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(this.newUser)
                });

                if (!response.ok) {
                    throw new Error('创建用户失败');
                }

                const result = await response.json();
                if (result.code === 200) {
                    this.showAddModal = false;
                    this.handleCancelAdd(); // 清空表单
                    this.fetchUsers(); // 刷新列表
                } else {
                    throw new Error(result.message || '创建用户失败');
                }
            } catch (err) {
                console.error('创建用户失败:', err);
                this.error = '创建用户失败: ' + err.message;
            }
        },
        validateUser(user) {
            if (!user.username) {
                this.error = '用户名不能为空';
                return false;
            }
            if (!user.password && !user.id) {
                // 新增时密码必填
                this.error = '密码不能为空';
                return false;
            }
            if (!user.email) {
                this.error = '邮箱不能为空';
                return false;
            }
            if (!user.realName) {
                this.error = '真实姓名不能为空';
                return false;
            }
            // 清除错误提示
            this.error = null;
            return true;
        }
    },
    mounted() {
        // 添加样式到页面
        if (!document.getElementById('user-management-styles')) {
            const styleElement = document.createElement('style');
            styleElement.id = 'user-management-styles';
            styleElement.textContent = styles.styles;
            document.head.appendChild(styleElement);
        }
        this.fetchUsers();
    }
});
