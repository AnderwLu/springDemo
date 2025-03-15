const BatchImport = {
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
  data() {
    return {
      fileName: '',
      success: '',
      error: ''
    }
  },
  mounted() {
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
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.fileName = file.name;
      } else {
        this.fileName = '';
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
    }
  }
}; 