# 1️⃣ ĐOÀN XUÂN TRƯỜNG

## Chức năng

* Authentication (Customer + Admin/Staff)
* JWT + Phân quyền
* WebSocket cấu hình nền tảng
* Quản lý User hệ thống

## Nhánh

* feature/auth-security
* feature/websocket-core
* feature/user-management

## Làm trước

Toàn bộ project

## Phối hợp

Tất cả thành viên

## File phụ trách

### config/

* SecurityConfig.java
* WebSocketConfig.java
* CorsConfig.java

### security/

* JwtAuthenticationFilter.java

### util/

* JwtUtil.java

### controller/

* AuthController.java
* CustomerAuthController.java
* UserController.java

### service/

* AuthService.java / AuthServiceImpl.java
* UserService.java / UserServiceImpl.java

### repository/

* UserRepository.java

### mapper/

* UserMapper.java

### dto liên quan auth + user

---

## Hoàn chỉnh API

### Customer

* POST /api/customers/register
* POST /api/customers/login

### Admin/Staff

* POST /api/auth/login

### User

* GET /api/users/role/{role}
* PATCH /api/users/{id}/password
* PUT /api/users/{id}

## Kết quả

* Đăng nhập JWT hoạt động
* Phân quyền ADMIN / STAFF / CUSTOMER
* WebSocket cấu hình sẵn sàng

---