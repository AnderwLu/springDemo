export default {
    styles: `
        .user-management {
            display: flex;
            flex-direction: column;
            min-width: 0;
            min-height: 0;
            height: 100%;
        }

        .card {
            flex: 1;
            display: flex;
            flex-direction: column;
            min-width: 0;
            min-height: 0;
            border: 1px solid #e5e7eb;
            border-radius: 0.5rem;
            background-color: #fff;
            box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
        }

        .card-body {
            flex: 1;
            display: flex;
            flex-direction: column;
            min-width: 0;
            min-height: 0;
            padding: 1rem;
            overflow: hidden;
        }

        /* 搜索表单样式 */
        .search-form {
            flex: none;
            background-color: #f8f9fa;
            border-radius: 0.5rem;
            margin-bottom: 1rem;
            padding: 1rem;
        }

        /* 表格容器样式 */
        .table-responsive {
            flex: 1;
            display: flex;
            flex-direction: column;
            min-width: 0;
            min-height: 0;
            border: 1px solid #e5e7eb;
            border-radius: 0.5rem;
            background-color: #fff;
            margin-bottom: 1rem;
            overflow: hidden;
        }

        /* 表格横向滚动容器 */
        .table-container {
            flex: 1;
            overflow: auto;
            min-width: 0;
            min-height: 0;
        }

        /* 表格样式优化 */
        .table {
            margin-bottom: 0;
            border-collapse: separate;
            border-spacing: 0;
            background-color: #fff;
        }

        /* 表头样式 */
        .table > thead > tr > th {
            white-space: nowrap;
            background-color: #f8f9fa;
            font-weight: 700;
            padding: 1rem 0.75rem;
            border: none;
            border-bottom: 2px solid #e5e7eb;
            position: sticky;
            top: 0;
            z-index: 1;
            color: #374151;
            font-size: 0.875rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            height: 56px;
            line-height: 1.2;
        }

        /* 表格单元格样式 */
        .table > tbody > tr > td {
            padding: 1rem 0.75rem;
            vertical-align: middle;
            border: none;
            border-bottom: 1px solid #e5e7eb;
            color: #4b5563;
            font-size: 0.875rem;
            height: 56px;
            line-height: 1.2;
            background-color: #fff;
        }

        /* 表格行悬停效果 */
        .table > tbody > tr:hover > td {
            background-color: #f8f9fa;
        }

        /* 移除斑马纹样式 */
        .table-striped > tbody > tr:nth-of-type(odd) > td {
            background-color: #fff;
        }

        .table-striped > tbody > tr:nth-of-type(odd):hover > td {
            background-color: #f8f9fa;
        }

        /* 文本溢出处理 */
        .table .cell-ellipsis {
            max-width: 0;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

       

        /* Badge样式 */
        .badge {
            font-weight: normal;
            padding: 0.4em 0.6em;
        }

        /* 按钮样式 */
        .btn-group .btn {
            padding: 0.25rem 0.5rem;
        }

        /* 模态框样式优化 */
        .modal-dialog {
            max-width: 500px;
        }

        .modal-body {
            max-height: calc(100vh - 210px);
            overflow-y: auto;
        }

        /* 自定义滚动条样式 */
        .table-container::-webkit-scrollbar {
            height: 8px;
        }

        .table-container::-webkit-scrollbar-track {
            background: #f1f1f1;
            border-radius: 4px;
        }

        .table-container::-webkit-scrollbar-thumb {
            background: #c1c1c1;
            border-radius: 4px;
        }

        .table-container::-webkit-scrollbar-thumb:hover {
            background: #a8a8a8;
        }

        /* 空数据样式 */
        .table .empty-row td {
            padding: 2rem;
            text-align: center;
            color: #6b7280;
            background-color: #fff !important;
        }

        .table .empty-row i {
            font-size: 2rem;
            margin-bottom: 0.5rem;
            color: #9ca3af;
        }
    `
};
