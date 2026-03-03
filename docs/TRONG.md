# 3️⃣ HỒ NGỌC TRỌNG

## Chức năng

Quản lý bàn

## Nhánh

* feature/table

## Làm sau

Sau Authentication
Song song với Category/Dish

## Phối hợp

Phối hợp Nguyễn Đức Tuấn (liên kết hóa đơn)

## File phụ trách

### entity

* TableEntity.java
* TableStatus.java

### repository

* TableRepository.java

### mapper

* TableMapper.java

### service

* TableService.java / Impl

### controller

* TableController.java

### dto/table

---

## Hoàn chỉnh API

* GET /api/tables
* GET /api/tables/{id}
* GET /api/tables/number/{tableNumber}
* GET /api/tables/status/{status}
* GET /api/tables/area/{area}
* GET /api/tables/active
* POST /api/tables
* PUT /api/tables/{id}
* PATCH /api/tables/{id}/status
* DELETE /api/tables/{id}

## Kết quả

Quản lý trạng thái bàn: AVAILABLE / OCCUPIED

---