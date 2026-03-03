# 4️⃣ NGUYỄN ĐỨC TUẤN

## Chức năng

Hóa đơn + Món trong hóa đơn + Thanh toán

## Nhánh

* feature/invoice
* feature/payment

## Làm sau

Sau khi Table hoàn thành

## Phối hợp

* Hồ Ngọc Trọng (Table)
* Đoàn Gia Hân (Notification)
* Đoàn Xuân Trường (Security)

## File phụ trách

### entity

* InvoiceEntity.java
* InvoiceItemEntity.java
* PaymentEntity.java
* InvoiceStatus.java
* PaymentStatus.java
* PaymentMethod.java

### repository

* InvoiceRepository.java
* InvoiceItemRepository.java
* PaymentRepository.java

### mapper

* InvoiceMapper.java
* InvoiceItemMapper.java
* PaymentMapper.java

### service

* InvoiceService.java / Impl
* InvoiceItemService.java / Impl
* PaymentService.java / Impl

### controller

* InvoiceController.java
* InvoiceItemController.java
* PaymentController.java

### dto/invoice

### dto/invoiceitem

### dto/payment

---

## Hoàn chỉnh API

### Invoice

* GET /api/invoices
* GET /api/invoices/table/{tableId}/active
* GET /api/invoices/date-range
* POST /api/invoices/create-with-items
* PATCH /api/invoices/{id}/status

### Invoice Item

* GET /api/invoice-items/invoice/{invoiceId}
* POST /api/invoice-items/add-to-invoice
* PATCH /api/invoice-items/{id}/quantity

### Payment

* GET /api/payments/invoice/{invoiceId}
* POST /api/payments/process
* PATCH /api/payments/{id}/confirm
* PATCH /api/payments/{id}/cancel

## Kết quả

* Tạo hóa đơn theo bàn
* Thêm món
* Thanh toán và cập nhật trạng thái

---