# 🍽 ORDER TABLE BACKEND

Hệ thống gọi món tại bàn qua QR Code
Spring Boot – JPA – JWT – WebSocket

---

# I. THỨ TỰ TRIỂN KHAI

1. Đoàn Xuân Trường – Authentication + Security + WebSocket nền tảng
2. Phạm Long Vũ – Category + Dish
3. Hồ Ngọc Trọng – Table
4. Nguyễn Đức Tuấn – Invoice + InvoiceItem + Payment
5. Đoàn Gia Hân – Notification + Message + Realtime nghiệp vụ

---

# II. PHÂN CHIA CÔNG VIỆC CHI TIẾT

---
Tại các file md

# 📊 SƠ ĐỒ TRIỂN KHAI CHI TIẾT (CHIỀU NGANG)

```
THỜI GIAN  ───────────────────────────────────────────────────────────────────────────────▶


GIAI ĐOẠN 1
───────────────────────────────────────────────────────────────────────────────────────────
ĐOÀN XUÂN TRƯỜNG
[ Authentication ]
[ JWT + SecurityConfig ]
[ Role + User ]
[ WebSocketConfig ]
───────────────────────────────────────────────────────────────────────────────────────────


GIAI ĐOẠN 2 (Song Song Sau Khi Giai Đoạn 1 Hoàn Thành)
───────────────────────────────────────────────────────────────────────────────────────────
PHẠM LONG VŨ                    HỒ NGỌC TRỌNG
[ Category CRUD ]               [ Table CRUD ]
[ Dish CRUD ]                   [ Table Status ]
                                                
( Hai phần này làm song song, không phụ thuộc nhau )
───────────────────────────────────────────────────────────────────────────────────────────


GIAI ĐOẠN 3 (Phụ Thuộc Category + Dish + Table)
───────────────────────────────────────────────────────────────────────────────────────────
NGUYỄN ĐỨC TUẤN
                [ Invoice ]
                [ InvoiceItem ]
                [ Payment ]
───────────────────────────────────────────────────────────────────────────────────────────


GIAI ĐOẠN 4 (Phụ Thuộc Invoice + WebSocket Core)
───────────────────────────────────────────────────────────────────────────────────────────
ĐOÀN GIA HÂN
                              [ Notification ]
                              [ Message ]
                              [ Realtime Events ]
───────────────────────────────────────────────────────────────────────────────────────────
```

End README
