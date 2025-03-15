-- 初始数据
-- 这个文件可以用来插入初始数据

-- 插入管理员用户（如果不存在）
INSERT INTO users (username, password, email, role, enabled)
SELECT 'admin', '$2a$10$rDkPvvAFV6GgJdnUSZQKJeP0NdQJ12xg1Z7VZU.CWUVr6eRZKaU5m', 'admin@example.com', 'ROLE_ADMIN', true
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin'); 