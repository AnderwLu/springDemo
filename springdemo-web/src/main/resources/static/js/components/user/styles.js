export default {
    styles: `
        .required:after {
            content: ' *';
            color: red;
        }

        /* 表格容器样式 */
        .table-responsive {
            max-height: 600px;
            overflow-y: auto;
        }

        /* 表格横向滚动容器 */
        .table-container {
            overflow-x: auto;
            border: 1px solid #e5e7eb;
            border-radius: 0.5rem;
            background-color: #fff;
            box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
        }

        /* 表格样式优化 */
        .table {
            width: 100%;
            margin-bottom: 0;
            white-space: nowrap;
            border-collapse: separate;
            border-spacing: 0;
            background-color: #fff;
        }

        /* 表头样式 */
        .table > thead > tr > th {
            white-space: nowrap;
            background-color: #f8f9fa;
            font-weight: 500;
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

        /* 固定宽度列 */
        .table .col-id {
            width: 80px;
            min-width: 80px;
        }

        .table .col-username {
            width: 120px;
            min-width: 120px;
        }

        .table .col-realname {
            width: 100px;
            min-width: 100px;
        }

        .table .col-email {
            width: 200px;
            min-width: 200px;
        }

        .table .col-phone {
            width: 120px;
            min-width: 120px;
        }

        .table .col-gender {
            width: 80px;
            min-width: 80px;
        }

        .table .col-age {
            width: 80px;
            min-width: 80px;
        }

        .table .col-address {
            min-width: 200px;
        }

        .table .col-role,
        .table .col-status {
            width: 100px;
            min-width: 100px;
        }

        .table .col-actions {
            width: 120px;
            min-width: 120px;
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
