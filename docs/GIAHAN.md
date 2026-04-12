56x# 5️⃣ ĐOÀN GIA HÂN

## Chức năng

Notification + Message + Realtime nghiệp vụ

## Nhánh

* feature/notification
* feature/message

## Làm sau

* Sau WebSocket core
* Sau Invoice hoàn thành

## Phối hợp

* Nguyễn Đức Tuấn (event hóa đơn)
* Đoàn Xuân Trường (WebSocket config)

## File phụ trách

### entity

* NotificationEntity.java
* MessageEntity.java
* MessageType.java
* MessageSender.java
* RecipientType.java

### repository

* NotificationRepository.java
* MessageRepository.java

### mapper

* NotificationMapper.java
* MessageMapper.java

### service

* NotificationService.java / Impl
* MessageService.java / Impl
* WebSocketService.java

### controller

* NotificationController.java
* MessageController.java

### websocket/

* WebSocketController.java
* WebSocketEventListener.java
* WebSocketMessage.java

### dto/notification

### dto/message

---

## Hoàn chỉnh API

Notification:

* Tạo thông báo
* Đánh dấu đã đọc

Message:

* Gửi tin nhắn giữa bàn – bếp – nhân viên

Realtime:

* Thông báo khi tạo hóa đơn
* Thông báo khi thêm món
* Thông báo khi thanh toán

## Kết quả

Hệ thống realtime hoạt động hoàn chỉnh

---

# III. HOÀN THÀNH HỆ THỐNG KHI

* Login phân quyền hoạt động
* Admin quản lý món và bàn
* Tạo hóa đơn theo bàn
* Thêm món và cập nhật trạng thái
* Thanh toán hoàn tất
* Thông báo realtime hoạt động

---