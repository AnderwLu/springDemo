/**
 * SpringDemo管理后台JS
 */
$(document).ready(function() {
    // 侧边栏菜单展开/折叠
    $('.menu-item > a').on('click', function(e) {
        const $menuItem = $(this).parent('.menu-item');
        
        if ($menuItem.find('.submenu').length) {
            e.preventDefault();
            
            if ($menuItem.hasClass('open')) {
                $menuItem.removeClass('open');
            } else {
                // 关闭其他已打开的菜单
                $('.menu-item.open').removeClass('open');
                $menuItem.addClass('open');
            }
        }
    });

    // 移动端侧边栏切换
    $('.menu-toggle').on('click', function() {
        $('.sidebar').toggleClass('show');
        $('.main-content').toggleClass('sidebar-open');
    });

    // 表格行选择
    $('.table-selectable tbody tr').on('click', function() {
        $(this).toggleClass('selected');
    });

    // 全选/取消全选
    $('#select-all').on('change', function() {
        if ($(this).is(':checked')) {
            $('.table-selectable tbody tr').addClass('selected');
            $('.table-selectable tbody .checkbox-item').prop('checked', true);
        } else {
            $('.table-selectable tbody tr').removeClass('selected');
            $('.table-selectable tbody .checkbox-item').prop('checked', false);
        }
    });

    // 标签页切换
    $('.tab-item').on('click', function() {
        const target = $(this).data('target');
        
        // 切换标签页激活状态
        $('.tab-item').removeClass('active');
        $(this).addClass('active');
        
        // 切换内容区域
        $('.tab-content').removeClass('active');
        $(target).addClass('active');
    });

    // 文件上传区域交互
    $('.file-upload-area').on('click', function() {
        $(this).find('input[type="file"]').click();
    });

    $('input[type="file"]').on('change', function() {
        const fileName = $(this).val().split('\\').pop();
        if (fileName) {
            const $uploadArea = $(this).closest('.file-upload-area');
            $uploadArea.addClass('has-file');
            $uploadArea.find('.file-name').text(fileName);
        }
    });

    // 拖拽上传
    $('.file-upload-area').on('dragover', function(e) {
        e.preventDefault();
        $(this).addClass('dragover');
    });

    $('.file-upload-area').on('dragleave', function(e) {
        e.preventDefault();
        $(this).removeClass('dragover');
    });

    $('.file-upload-area').on('drop', function(e) {
        e.preventDefault();
        $(this).removeClass('dragover');
        
        const files = e.originalEvent.dataTransfer.files;
        if (files.length) {
            const $fileInput = $(this).find('input[type="file"]');
            $fileInput[0].files = files;
            $fileInput.trigger('change');
        }
    });

    // 批处理作业操作
    $('.job-action').on('click', function() {
        const action = $(this).data('action');
        const jobId = $(this).data('job-id');
        
        // 确认操作
        if (action === 'stop' || action === 'delete') {
            if (!confirm('确定要' + (action === 'stop' ? '停止' : '删除') + '该作业吗？')) {
                return;
            }
        }
        
        // 发送请求
        $.ajax({
            url: '/admin/batch/jobs/' + jobId + '/' + action,
            type: 'POST',
            success: function(response) {
                if (response.success) {
                    alert('操作成功');
                    location.reload();
                } else {
                    alert('操作失败: ' + response.message);
                }
            },
            error: function() {
                alert('操作失败，请稍后重试');
            }
        });
    });

    // 初始化提示框
    $('[data-toggle="tooltip"]').tooltip();

    // 初始化日期选择器
    if ($.fn.datepicker) {
        $('.datepicker').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
            language: 'zh-CN'
        });
    }

    // 初始化下拉选择框
    if ($.fn.select2) {
        $('.select2').select2({
            placeholder: '请选择',
            allowClear: true
        });
    }
});

/**
 * 管理员界面JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Admin interface loaded');
    
    // 侧边栏折叠/展开功能
    const sidebarCollapse = document.getElementById('sidebarCollapse');
    if (sidebarCollapse) {
        sidebarCollapse.addEventListener('click', function() {
            document.getElementById('sidebar').classList.toggle('active');
            document.getElementById('content').classList.toggle('active');
        });
    }
    
    // 自动展开当前活动的子菜单
    const activeSubmenuItem = document.querySelector('#sidebar .collapse li.active');
    if (activeSubmenuItem) {
        const parentCollapse = activeSubmenuItem.closest('.collapse');
        if (parentCollapse) {
            parentCollapse.classList.add('show');
        }
    }
    
    // 初始化工具提示
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // 初始化弹出框
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // 可选择的表格行
    const selectableRows = document.querySelectorAll('.table-selectable tbody tr');
    selectableRows.forEach(row => {
        row.addEventListener('click', function() {
            this.classList.toggle('selected');
        });
    });
    
    // 表格排序功能
    const sortableTables = document.querySelectorAll('.table-sortable');
    sortableTables.forEach(table => {
        const headers = table.querySelectorAll('th[data-sort]');
        headers.forEach(header => {
            header.addEventListener('click', function() {
                const sortKey = this.getAttribute('data-sort');
                const sortDirection = this.classList.contains('sort-asc') ? 'desc' : 'asc';
                
                // 移除所有排序类
                headers.forEach(h => {
                    h.classList.remove('sort-asc', 'sort-desc');
                });
                
                // 添加当前排序类
                this.classList.add(`sort-${sortDirection}`);
                
                // 这里可以添加实际的排序逻辑
                console.log(`Sorting by ${sortKey} in ${sortDirection} order`);
            });
        });
    });
}); 