// 导入Vue的必要函数
const { defineComponent } = Vue;

export default defineComponent({
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
            }
        };
    },
    methods: {
        async fetchUsers() {
            console.log('Fetching users...');
            this.loading = true;
            try {
                // 构建查询参数
                const params = new URLSearchParams();
                if (this.queryParams.username) {
                    params.append('username', this.queryParams.username);
                }
                if (this.queryParams.email) {
                    params.append('email', this.queryParams.email);
                }

                const response = await fetch(`/springdemo/api/users?${params.toString()}`);
                if (!response.ok) {
                    throw new Error('获取用户数据失败');
                }
                this.users = await response.json();
                console.log('Users loaded:', this.users);
            } catch (err) {
                this.error = err.message;
                console.error('Error fetching users:', err);
            } finally {
                this.loading = false;
            }
        },
        handleSearch() {
            console.log('Searching with params:', this.queryParams);
            this.fetchUsers();
        },
        resetSearch() {
            console.log('Resetting search');
            this.queryParams.username = '';
            this.queryParams.email = '';
            this.fetchUsers();
        }
    },
    created() {
        console.log('UserManagement component created');
    },
    mounted() {
        console.log('UserManagement component mounted');
        this.fetchUsers();
    }
});
