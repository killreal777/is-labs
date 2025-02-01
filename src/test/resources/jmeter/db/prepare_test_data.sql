TRUNCATE TABLE users CASCADE;

SELECT setval('users_id_seq', 1, false);
SELECT setval('marines_id_seq', 1, false);
SELECT setval('marines_import_id_seq', 1, false);
SELECT setval('chapters_id_seq', 1, false);
SELECT setval('chapters_import_id_seq', 1, false);

INSERT INTO users (username, password, role, enabled) VALUES
('admin1', '4AzyWtQmg7PfZ4xh9Cxr2g==', 'ROLE_ADMIN', true), -- admin1
('admin2', 'yEJY6cOQWaiat32Ebdq5CQ==', 'ROLE_ADMIN', true), -- admin2
('user3', 'kod69wpF/Wou1/6B4SNreA==', 'ROLE_USER', true), -- user3
('user4', 'PwLr49eSmwkePYzP3i87xg==', 'ROLE_USER', true), -- user4
('user5', 'CnkYQvUqCs+7OngzeMBmuA==', 'ROLE_USER', true); -- user5

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));