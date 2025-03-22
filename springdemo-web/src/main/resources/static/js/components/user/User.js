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
            showAddModal: false,
            exporting: false
        };
    },
    methods: {
        async fetchUsers() {
            console.log('开始获取用户数据...');
            this.loading = true;
            try {
                const params = {
                    page: this.pagination.page,
                    size: this.pagination.size
                };

                if (this.queryParams.username) {
                    params.username = this.queryParams.username;
                }
                if (this.queryParams.email) {
                    params.email = this.queryParams.email;
                }

                console.log('请求参数:', params);

                const response = await axios.get('/springdemo/api/users', { params });
                console.log('API原始响应:', response);

                // 检查数据是否符合 { rows: [...], total: number } 格式
                if (response && response.rows && Array.isArray(response.rows)) {
                    console.log('数据格式正确，共' + response.rows.length + '条，总计' + response.total + '条');
                    this.users = response.rows;
                    this.pagination.totalElements = response.total;
                    this.pagination.totalPages = Math.ceil(response.total / this.pagination.size);
                } else {
                    console.error('响应格式不符合预期:', data);
                    this.error = '数据格式不正确';
                }
            } catch (err) {
                this.error = err.message || '获取用户数据失败';
                console.error('获取用户数据出错:', err);
            } finally {
                this.loading = false;
            }
        },
        handlePageChange(page) {
            this.pagination.page = page;
            this.fetchUsers();
        },
        handleSizeChange(size) {
            this.pagination.size = size;
            this.pagination.page = 0; // 改变每页显示数量后，回到第一页
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

                        // 使用 axios 直接提交 FormData
                        await axios.post('/springdemo/api/users/import', formData, {
                            headers: {
                                'Content-Type': 'multipart/form-data'
                            }
                        });

                        // 如果没有抛出异常，说明导入成功
                        this.fetchUsers(); // 刷新用户列表
                    }
                    document.body.removeChild(input);
                };

                input.click();
            } catch (err) {
                console.error('导入用户数据失败:', err);
                this.error = '导入用户数据失败: ' + err.message;
            }
        },
        async handleExport(event) {
            if (this.exporting) {
                return; // 防止重复点击
            }

            this.exporting = true;

            try {
                // 创建飞入动画
                this.createFlyAnimation(event);

                const params = new URLSearchParams();
                if (this.queryParams.username) {
                    params.append('username', this.queryParams.username);
                }
                if (this.queryParams.email) {
                    params.append('email', this.queryParams.email);
                }

                // 使用浏览器原生方式下载
                const downloadUrl = `/springdemo/api/users/export?${params.toString()}`;

                // 使用<a>标签直接触发下载，不处理响应内容
                const a = document.createElement('a');
                a.href = downloadUrl;
                // 不设置download属性，让浏览器使用服务器提供的文件名
                // a.download = filename;

                // 添加到DOM并触发点击
                document.body.appendChild(a);
                a.click();

                // 清理DOM
                document.body.removeChild(a);
            } catch (err) {
                console.error('导出用户数据失败:', err);
                this.error = '导出用户数据失败: ' + err.message;
            } finally {
                // 延迟重置状态，防止短时间内多次点击
                setTimeout(() => {
                    this.exporting = false;
                }, 1000);
            }
        },
        /**
         * 创建按钮点击动画效果
         *
         * 当用户点击导出按钮时，创建一个从按钮中心向外扩散的动画效果
         * 提供视觉反馈，增强用户体验
         *
         * @param {Event} event - 点击事件对象，用于获取按钮位置
         */
        createFlyAnimation(event) {
            // 只是在按钮周围创建一个扩散效果，而不是飞向特定位置
            const button = event.target.closest('button');
            const rect = button.getBoundingClientRect();
            const ripple = document.createElement('div');

            // 设置涟漪元素的样式
            // 将元素定位在按钮中心位置
            ripple.style.cssText = `
                position: fixed; /* 使用fixed定位，相对于视口固定位置，确保动画效果不受页面滚动影响 */
                left: ${rect.left + rect.width / 2}px;
                top: ${rect.top + rect.height / 2}px;
                width: 20px;
                height: 20px;
                background-color: rgba(54, 153, 255, 0.5);
                border-radius: 50%;
                transform: translate(-50%, -50%) scale(1);
                z-index: 9999;
                pointer-events: none;
            `;

            // 将涟漪元素添加到DOM中
            document.body.appendChild(ripple);

            // 延迟一小段时间后开始动画
            // 这样可以确保元素已经被渲染，动画效果更流畅
            setTimeout(() => {
                ripple.style.transition = 'all 0.5s ease-out';
                ripple.style.transform = 'translate(-50%, -50%) scale(3)';
                ripple.style.opacity = '0';
            }, 50);

            // 动画结束后移除涟漪元素，避免DOM污染
            setTimeout(() => {
                document.body.removeChild(ripple);
            }, 600);
        },
        async handleEdit(user) {
            try {
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

                // 使用全局 $http 对象
                await this.$http.delete(`/springdemo/api/users/${user.id}`);

                // 删除成功，刷新用户列表
                this.fetchUsers();
            } catch (err) {
                console.error('删除用户失败:', err);
                this.error = '删除用户失败: ' + err.message;
            }
        },
        async handleSaveEdit() {
            try {
                // 验证用户数据
                const validationError = this.validateUser(this.editingUser);
                if (validationError) {
                    this.error = validationError;
                    return;
                }

                // 使用全局 $http 对象
                await this.$http.put(`/springdemo/api/users/${this.editingUser.id}`, this.editingUser);

                // 编辑成功，关闭模态框并刷新列表
                this.showEditModal = false;
                this.fetchUsers();
            } catch (err) {
                console.error('保存用户失败:', err);
                this.error = '保存用户失败: ' + err.message;
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
                    this.fetchUsers();
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
                // 验证用户数据
                const validationError = this.validateUser(this.newUser);
                if (validationError) {
                    this.error = validationError;
                    return;
                }

                // 使用全局 $http 对象
                await this.$http.post('/springdemo/api/users', this.newUser);

                // 添加成功，关闭模态框并刷新列表
                this.showAddModal = false;
                this.resetNewUser();
                this.fetchUsers();
            } catch (err) {
                console.error('添加用户失败:', err);
                this.error = '添加用户失败: ' + err.message;
            }
        },
        validateUser(user) {
            if (!user.username) {
                this.error = '用户名不能为空';
                return false;
            }
            if (!user.password && !user.id) {
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
            this.error = null;
            return true;
        }
    },
    mounted() {
        if (!document.getElementById('user-management-styles')) {
            const styleElement = document.createElement('style');
            styleElement.id = 'user-management-styles';
            styleElement.textContent = styles.styles;
            document.head.appendChild(styleElement);
        }
        this.fetchUsers();
    }
});
