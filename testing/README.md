Cấu trúc đề xuất cho Chương 3
# CHƯƠNG 3. THIẾT KẾ TEST CASE

## 3.1. Phương pháp thiết kế test case
## 3.2. Black-box Testing
### 3.2.1. Phân lớp tương đương
### 3.2.2. Phân tích giá trị biên
### 3.2.3. Chuyển trạng thái / Tổ hợp điều kiện
## 3.3. White-box Testing
### 3.3.1. Module được chọn để phân tích CFG
### 3.3.2. Vẽ Control Flow Graph
### 3.3.3. Tính Cyclomatic Complexity
### 3.3.4. Xác định độ bao phủ
### 3.3.5. Danh sách White-box Test Cases
## 3.4. Kết luận chương
Nhưng trước khi mình generate toàn bộ 90 test case cho bạn:

Vì đây là số lượng rất lớn (90 Black-box + 40 White-box), để làm đúng kỹ thuật và logic, mình cần bạn xác nhận 2 thứ để tránh làm sai hướng:

1. Bạn muốn test những module/chức năng nào là trọng tâm?

Vì hệ thống của bạn khá lớn. Thường nên chọn 5–7 chức năng core để test sâu, ví dụ:

Đăng ký / Đăng nhập
Quản lý giỏ hàng
Đặt món
Thanh toán
Quản lý đơn hàng
Chat realtime
Quản lý bàn
CRUD món ăn / danh mục

👉 Bạn hãy chọn giúp mình các module muốn tập trung kiểm thử
(hoặc nói “test toàn hệ thống” nếu muốn mình tự phân bổ)

2. White-box Testing bạn muốn phân tích CFG cho những hàm/backend nào?

Vì White-box phải dựa trên code thật / logic thật.
Muốn vẽ CFG chuẩn và ra 40 test case hợp lý, mình cần biết bạn muốn kiểm thử white-box cho hàm nào, ví dụ:

createOrder()
processPayment()
login()
updateOrderStatus()
assignTable()
sendMessage()

👉 Bạn nên gửi code / pseudo-code / logic của 1–3 hàm backend quan trọng
để mình vẽ CFG chuẩn cho bạn.

Vì sao cần xác nhận trước?

Nếu mình tự bịa test case/CFG chung chung:

Dễ không khớp hệ thống thật
Giảng viên hỏi phản biện sẽ khó trả lời
White-box rất dễ bị bắt lỗi nếu CFG không match code thực tế
Gợi ý chuyên môn

Với đồ án của bạn, mình đề xuất:

Black-box (90 TC)

Phân bổ:

Auth: 8
Menu/Search: 6
Cart: 8
Order: 10
Payment: 6
Table Management: 6
Chat/Realtime: 6
White-box (40 TC)

Chọn 4 hàm core:

login() → 10 TC
createOrder() → 10 TC
updateOrderStatus() → 10 TC
processPayment() → 10 TC

=> Đây là cấu trúc rất đẹp để chấm rubric.

Chỉ cần bạn trả lời theo mẫu này:
Black-box focus:
1. ...
2. ...
3. ...

White-box functions:
1. ...
2. ...
3. ...

(Optional) Đây là code/pseudocode:
...