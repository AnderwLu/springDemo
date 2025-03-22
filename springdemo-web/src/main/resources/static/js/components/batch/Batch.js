const { defineComponent } = Vue;

export default defineComponent({
    name: 'BatchManagement',
    template: '#batch-template',
    data() {
        return {
            error: null,
            importing: false,
            importProgress: 0,
            importHistory: []
        };
    },
    methods: {
        async handleImport() {
            try {
                const input = document.createElement('input');
                input.type = 'file';
                input.accept = '.xlsx,.xls';
                input.style.display = 'none';
                document.body.appendChild(input);

                input.onchange = async e => {
                    const file = e.target.files[0];
                    if (file) {
                        this.importing = true;
                        this.importProgress = 0;
                        const formData = new FormData();
                        formData.append('file', file);

                        try {
                            const response = await fetch('/springdemo/api/batch/import', {
                                method: 'POST',
                                body: formData
                            });

                            if (!response.ok) {
                                throw new Error('导入失败');
                            }

                            const result = await response.json();
                            if (result.code === 200) {
                                this.importHistory.unshift({
                                    id: Date.now(),
                                    fileName: file.name,
                                    importTime: new Date().toLocaleString(),
                                    status: '成功',
                                    result: `成功导入 ${result.data.successCount} 条记录`
                                });
                            } else {
                                throw new Error(result.message || '导入失败');
                            }
                        } catch (err) {
                            this.importHistory.unshift({
                                id: Date.now(),
                                fileName: file.name,
                                importTime: new Date().toLocaleString(),
                                status: '失败',
                                result: err.message
                            });
                            throw err;
                        } finally {
                            this.importing = false;
                            this.importProgress = 100;
                        }
                    }
                    document.body.removeChild(input);
                };

                input.click();
            } catch (err) {
                console.error('导入失败:', err);
                this.error = '导入失败: ' + err.message;
            }
        },

        async handleExport() {
            try {
                const response = await fetch('/springdemo/api/batch/export', {
                    method: 'GET'
                });

                if (!response.ok) {
                    throw new Error('导出失败');
                }

                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                const timestamp = new Date()
                    .toISOString()
                    .replace(/[^0-9]/g, '')
                    .slice(0, 14);
                a.href = url;
                a.download = `export_${timestamp}.xlsx`;
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
            } catch (err) {
                console.error('导出失败:', err);
                this.error = '导出失败: ' + err.message;
            }
        }
    },
    mounted() {
        // 动态加载组件样式
        if (!document.getElementById('batch-component-styles')) {
            const link = document.createElement('link');
            link.id = 'batch-component-styles';
            link.rel = 'stylesheet';
            link.href = '/springdemo/css/components/batch.css';
            document.head.appendChild(link);
        }
        this.fetchBatchJobs();
    },
    unmounted() {
        // 组件卸载时移除样式
        const styleSheet = document.getElementById('batch-component-styles');
        if (styleSheet) {
            styleSheet.remove();
        }
    }
});
