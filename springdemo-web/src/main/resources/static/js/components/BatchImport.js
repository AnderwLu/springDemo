export default {
    template: `
    <div class="card">
      <div class="card-header">
        <h2>批量导入用户</h2>
        <p class="text-muted">上传CSV文件导入用户数据</p>
      </div>
      <div class="card-body">
        <!-- 成功消息 -->
        <div class="alert alert-success" v-if="success" v-text="success"></div>
        
        <!-- 错误消息 -->
        <div class="alert alert-danger" v-if="error" v-text="error"></div>
        
        <!-- 文件上传表单 -->
        <form ref="uploadForm" action="/springdemo/batch/import-users" method="post" enctype="multipart/form-data" @submit.prevent="submitForm">
          <div class="mb-3">
            <label for="file" class="form-label">选择CSV文件</label>
            <div class="file-upload" @click="triggerFileInput">
              <input type="file" class="d-none" id="file" name="file" accept=".csv" @change="handleFileChange" ref="fileInput">
              <div v-if="!fileName">
                <i class="bi bi-cloud-arrow-up fs-3"></i>
                <p>点击选择文件或拖拽文件到此处</p>
              </div>
              <div v-else class="file-info">
                <p><strong>已选择文件:</strong> {{ fileName }}</p>
              </div>
            </div>
          </div>
          
          <div class="mb-3">
            <p class="text-muted">CSV文件格式要求:</p>
            <ul class="text-muted">
              <li>第一行必须是标题行: username,password,email,phone,realName,age,gender,address,role</li>
              <li>用户名和密码为必填项</li>
              <li>角色必须是ROLE_ADMIN或ROLE_USER</li>
            </ul>
          </div>
          
          <button type="submit" class="btn btn-primary" :disabled="!fileName">
            <i class="bi bi-upload me-2"></i>上传并导入
          </button>
        </form>
        
        <div class="mt-4">
          <a href="/springdemo/" class="btn btn-outline-secondary">返回首页</a>
        </div>
      </div>
    </div>
  `,
    props: ['systemInfo', 'breadcrumbs'],
    data() {
        return {
            importTypes: ['用户数据', '订单数据', '产品数据'],
            selectedType: '用户数据',
            file: null,
            fileName: '',
            importStatus: '',
            importProgress: 0,
            importing: false,
            importError: null,
            importResults: null,
            recentImports: [],
            success: '',
            error: ''
        };
    },
    mounted() {
        this.fetchBatchData();
        // 从URL参数中获取消息
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('success')) {
            this.success = urlParams.get('success');
        }
        if (urlParams.has('error')) {
            this.error = urlParams.get('error');
        }
    },
    methods: {
        fetchBatchData() {
            // 使用全局 $http 对象从 API 获取数据
            this.$http
                .get('/springdemo/api/admin/batch/import')
                .then(data => {
                    // 如果有导入类型，则更新
                    if (data.importTypes && Array.isArray(data.importTypes)) {
                        this.importTypes = data.importTypes;
                    }

                    // 如果有最近导入记录，则更新
                    if (data.recentImports && Array.isArray(data.recentImports)) {
                        this.recentImports = data.recentImports;
                    } else {
                        // 默认数据
                        this.recentImports = [
                            { id: 1, type: '用户数据', filename: 'users.xlsx', date: '2023-03-15 14:30', status: '成功', records: 50 },
                            { id: 2, type: '订单数据', filename: 'orders.xlsx', date: '2023-03-14 11:20', status: '失败', records: 0 }
                        ];
                    }
                })
                .catch(error => {
                    console.error('获取批量导入数据失败:', error.message);

                    // 设置默认数据
                    this.recentImports = [
                        { id: 1, type: '用户数据', filename: 'users.xlsx', date: '2023-03-15 14:30', status: '成功', records: 50 },
                        { id: 2, type: '订单数据', filename: 'orders.xlsx', date: '2023-03-14 11:20', status: '失败', records: 0 }
                    ];
                });
        },
        triggerFileInput() {
            this.$refs.fileInput.click();
        },
        handleFileChange(event) {
            const selectedFile = event.target.files[0];
            if (selectedFile) {
                this.file = selectedFile;
                this.fileName = selectedFile.name;
                this.importError = null;
                this.importResults = null;
            }
        },
        submitForm() {
            if (!this.fileName) {
                this.error = '请选择一个文件上传';
                return;
            }

            const formData = new FormData(this.$refs.uploadForm);

            // 使用fetch API提交表单
            fetch('/springdemo/batch/import-users', {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        this.success = data.success;
                        this.error = '';
                    } else if (data.error) {
                        this.error = data.error;
                        this.success = '';
                    }
                })
                .catch(error => {
                    this.error = '上传失败: ' + error.message;
                    this.success = '';
                });
        },
        simulateProgress() {
            // 模拟进度条
            this.importProgress = 0;
            const interval = setInterval(() => {
                this.importProgress += 10;
                if (this.importProgress >= 100) {
                    clearInterval(interval);
                    this.importing = false;
                    // 模拟导入完成
                    this.importStatus = '导入完成';
                    this.importResults = {
                        totalRows: 50,
                        successRows: 48,
                        failedRows: 2,
                        details: [
                            { row: 10, message: '数据格式不正确' },
                            { row: 23, message: '字段值超出范围' }
                        ]
                    };
                }
            }, 300);
        },
        async handleImport() {
            if (!this.file) {
                this.importError = '请选择文件';
                return;
            }

            this.importing = true;
            this.importStatus = '正在导入...';
            this.importError = null;
            this.importResults = null;

            try {
                const formData = new FormData();
                formData.append('file', this.file);
                formData.append('type', this.selectedType);

                // 模拟进度
                this.simulateProgress();

                // 使用全局 $http 对象从 API 导入数据
                // 这里只是模拟导入，实际应用中应该使用真实的API
                // await this.$http.post('/springdemo/api/batch/import', formData);

                // 导入完成后会由 simulateProgress 方法设置结果
            } catch (error) {
                this.importing = false;
                this.importError = `导入失败: ${error.message}`;
                this.importStatus = '导入失败';
            }
        },
        resetImport() {
            this.file = null;
            this.fileName = '';
            this.importStatus = '';
            this.importProgress = 0;
            this.importing = false;
            this.importError = null;
            this.importResults = null;

            // 重置文件输入
            const fileInput = this.$refs.fileInput;
            if (fileInput) {
                fileInput.value = '';
            }
        }
    }
};
