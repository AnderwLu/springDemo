export default {
    styles: `
        .user-management .table {
            width: 100%;
            margin-bottom: 0;
            white-space: nowrap;
            border-collapse: separate;
            border-spacing: 0;
            background-color: #fff;
        }

        .user-management .table > thead > tr > th {
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

        .user-management .table > tbody > tr > td {
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

        .user-management .table > tbody > tr:hover > td {
            background-color: #f8f9fa;
        }

        .user-management .table .cell-ellipsis {
            max-width: 0;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        /* 其他样式保持不变，但都加上 .user-management 前缀 */
    `
};
