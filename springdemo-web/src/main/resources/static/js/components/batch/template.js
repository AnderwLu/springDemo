export default `
<div class="user-management">
    <div class="card shadow-sm">
        <div class="card-header">
            <h3 class="card-title">批量数据处理</h3>
        </div>
        <div class="card-body">
            <!-- 导入导出按钮区域 -->
            <div class="mb-4">
                <div class="d-flex gap-2">
                    <button class="btn btn-primary" @click="handleImport">
                        <i class="fas fa-upload me-1"></i> 导入Excel
                    </button>
                    <button class="btn btn-success" @click="handleExport">
                        <i class="fas fa-download me-1"></i> 导出Excel
                    </button>
                </div>
            </div>

            <!-- 错误提示 -->
            <div v-if="error" class="alert alert-danger" role="alert">
                {{ error }}
            </div>

            <!-- 导入进度条 -->
            <div v-if="importing" class="progress mb-3">
                <div class="progress-bar progress-bar-striped progress-bar-animated" 
                     role="progressbar" 
                     :style="{ width: importProgress + '%' }"
                     :aria-valuenow="importProgress" 
                     aria-valuemin="0" 
                     aria-valuemax="100">
                    {{importProgress}}%
                </div>
            </div>

            <!-- 最近导入记录 -->
            <div v-if="importHistory.length > 0" class="mt-4">
                <h5>最近导入记录</h5>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>文件名</th>
                                <th>导入时间</th>
                                <th>状态</th>
                                <th>结果</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="record in importHistory" :key="record.id">
                                <td>{{record.fileName}}</td>
                                <td>{{record.importTime}}</td>
                                <td>
                                    <span :class="{'text-success': record.status === '成功', 
                                                 'text-danger': record.status === '失败'}">
                                        {{record.status}}
                                    </span>
                                </td>
                                <td>{{record.result}}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
`;
