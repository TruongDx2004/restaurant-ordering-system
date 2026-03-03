# 2️⃣ PHẠM LONG VŨ

## Chức năng

Quản lý danh mục và món ăn

## Nhánh

* feature/category
* feature/dish

## Làm sau

Sau khi Authentication hoàn thành

## Phối hợp

Làm việc độc lập, cần Role ADMIN

## File phụ trách

### entity

* CategoryEntity.java
* DishEntity.java
* DishStatus.java

### repository

* CategoryRepository.java
* DishRepository.java

### mapper

* CategoryMapper.java
* DishMapper.java

### service

* CategoryService.java / Impl
* DishService.java / Impl

### controller

* CategoryController.java
* DishController.java

### dto/category

### dto/dish

---

## Hoàn chỉnh API

### Category

* GET /api/categories
* GET /api/categories/{id}
* GET /api/categories/name/{name}
* POST /api/categories
* PUT /api/categories/{id}
* DELETE /api/categories/{id}

### Dish

* GET /api/dishes
* GET /api/dishes/{id}
* GET /api/dishes/status/{status}
* GET /api/dishes/search
* GET /api/dishes/category/{categoryId}
* POST /api/dishes
* PUT /api/dishes/{id}
* PATCH /api/dishes/{id}/status
* DELETE /api/dishes/{id}

## Kết quả

Admin quản lý món và danh mục hoàn chỉnh

---