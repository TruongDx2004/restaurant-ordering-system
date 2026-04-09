USE restaurant_db;

-- Sample Data for categories
INSERT INTO categories (name) VALUES 
('Khai vị'),
('Món chính'),
('Tráng miệng'),
('Đồ uống');

-- Sample Data for tables
INSERT INTO tables (table_number, area, status, is_active) VALUES 
(1, 'Khu A', 'AVAILABLE', TRUE),
(2, 'Khu A', 'AVAILABLE', TRUE),
(3, 'Khu B', 'AVAILABLE', TRUE),
(4, 'Khu B', 'AVAILABLE', TRUE),
(5, 'VIP', 'AVAILABLE', TRUE);

-- Sample Data for users (Staff/Admin)
-- Note: In a real app, passwords samples should be hashed. Here we use a bcrypt hash for "123456" for demonstration.
INSERT INTO users (email, password, name, phone, role) VALUES 
('admin@restaurant.com', '$2a$10$mWHoU7sdz0AtIkckrhX/KONiuViAHeZaNeLyZ9C3oGpZmRtM1HpKG', 'Quản trị viên', '0123456789', 'ADMIN'),
('staff1@restaurant.com', '$2a$10$mWHoU7sdz0AtIkckrhX/KONiuViAHeZaNeLyZ9C3oGpZmRtM1HpKG', 'Nhân viên 1', '0987654321', 'EMPLOYEE');

-- Sample Data for customer
INSERT INTO customer (full_name, phone, password, status) VALUES 
('Nguyễn Văn A', '0901112223', '$2a$10$mWHoU7sdz0AtIkckrhX/KONiuViAHeZaNeLyZ9C3oGpZmRtM1HpKG', 'ACTIVE'),
('Trần Thị B', '0904445556', '$2a$10$mWHoU7sdz0AtIkckrhX/KONiuViAHeZaNeLyZ9C3oGpZmRtM1HpKG', 'ACTIVE');

-- Sample Data for dishes
INSERT INTO dishes (name, price, status, image, category_id) VALUES 
('Gỏi cuốn', 55000, 'AVAILABLE', 'goi_cuon.jpg', 1),
('Phở bò', 75000, 'AVAILABLE', 'pho_bo.jpg', 2),
('Cơm tấm', 65000, 'AVAILABLE', 'com_tam.jpg', 2),
('Chè thái', 35000, 'AVAILABLE', 'che_thai.jpg', 3),
('Cà phê sữa đá', 25000, 'AVAILABLE', 'cafe.jpg', 4),
('Nước ép cam', 40000, 'AVAILABLE', 'nuoc_ep.jpg', 4);

-- Sample Data for restaurant_config
INSERT INTO restaurant_config (name, address, phone, email, opening_time, closing_time) VALUES 
('Nhà Hàng HUTECH', '475A Điện Biên Phủ, P.25, Q.Bình Thạnh, TP.HCM', '02871012345', 'contact@hutech.edu.vn', '08:00:00', '22:00:00');

-- Sample Data for invoices (Testing relationships)
INSERT INTO invoices (table_id, total_amount, status) VALUES 
(1, 130000, 'OPEN'),
(2, 25000, 'PAID');

-- Sample Data for invoice_items
INSERT INTO invoice_items (invoice_id, dish_id, quantity, unit_price, total_price, status, note, created_at, updated_at) VALUES 
(1, 2, 1, 75000, 75000, 'SERVED', 'Không hành', NOW(), NOW()),
(1, 1, 1, 55000, 55000, 'SERVED', '', NOW(), NOW()),
(2, 5, 1, 25000, 25000, 'SERVED', 'Ít đường', NOW(), NOW());

-- Sample Data for payments
INSERT INTO payments (invoice_id, amount, method, status, paid_at, transaction_code) VALUES 
(2, 25000, 'CASH', 'SUCCESS', NOW(), 'CASH-001');

-- Sample Data for notifications
INSERT INTO notifications (title, message, type, recipient_type, is_read) VALUES 
('Đơn hàng mới', 'Bàn số 1 vừa đặt món', 'NEW_ORDER', 'ROLE', FALSE);
