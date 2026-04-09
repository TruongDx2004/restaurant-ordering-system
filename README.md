# CHƯƠNG 4. KẾT LUẬN VÀ KIẾN NGHỊ

## 4.1 Kết luận

Sau quá trình nghiên cứu, phân tích, thiết kế và triển khai, đề tài “Hệ thống đặt món tại bàn thông minh” đã hoàn thành mục tiêu xây dựng một giải pháp hỗ trợ số hóa quy trình phục vụ trong nhà hàng trên nền tảng web. Hệ thống được phát triển dựa trên kiến trúc Client–Server với Backend sử dụng Spring Boot, cơ sở dữ liệu MySQL, giao tiếp thời gian thực thông qua WebSocket và cơ chế xác thực bảo mật bằng JWT Authentication.

Kết quả thực hiện cho thấy hệ thống đã triển khai thành công các chức năng nghiệp vụ cốt lõi phục vụ cho mô hình nhà hàng hiện đại, bao gồm đặt món thông qua QR Code, quản lý đơn hàng theo thời gian thực, quản lý menu và bàn ăn, theo dõi hóa đơn, hỗ trợ thanh toán trực tuyến và quản trị tập trung cho nhân viên và quản trị viên. Bên cạnh đó, đề tài còn mở rộng tích hợp thiết bị IoT hỗ trợ gọi món bằng giọng nói, góp phần nâng cao tính hiện đại và khả năng ứng dụng thực tiễn của hệ thống.

Thông qua quá trình thực hiện đề tài, nhóm đã vận dụng hiệu quả các kiến thức chuyên ngành về phát triển ứng dụng doanh nghiệp, thiết kế RESTful API, xử lý dữ liệu thời gian thực, bảo mật hệ thống, quản lý cơ sở dữ liệu và tích hợp các công nghệ mở rộng như AI và IoT. Đây là minh chứng cho khả năng áp dụng kiến thức lý thuyết vào giải quyết một bài toán thực tế trong lĩnh vực chuyển đổi số nhà hàng.

Nhìn chung, hệ thống đã đáp ứng được các mục tiêu nghiên cứu đặt ra ban đầu, đồng thời thể hiện tính khả thi trong việc triển khai thực tế tại các nhà hàng quy mô vừa và nhỏ. Đề tài không chỉ mang ý nghĩa học thuật trong việc nghiên cứu và ứng dụng công nghệ, mà còn có giá trị thực tiễn cao trong bối cảnh nhu cầu chuyển đổi số trong ngành dịch vụ ăn uống ngày càng gia tăng.

## 4.2 Những đóng góp của đề tài

Đề tài đã mang lại một số đóng góp đáng chú ý cả về mặt kỹ thuật lẫn ứng dụng thực tiễn. Trước hết, hệ thống xây dựng thành công một nền tảng đặt món tại bàn thông minh hoạt động trên nền tảng web, cho phép khách hàng tương tác trực tiếp với hệ thống mà không cần cài đặt ứng dụng. Đây là giải pháp phù hợp với xu hướng số hóa và tối ưu trải nghiệm người dùng trong ngành dịch vụ.

Bên cạnh đó, việc tích hợp công nghệ WebSocket giúp đồng bộ trạng thái đơn hàng theo thời gian thực giữa khách hàng và nhân viên, cải thiện tốc độ phản hồi và hiệu quả phục vụ. Ngoài ra, chức năng gọi món bằng giọng nói thông qua thiết bị IoT tích hợp AI là điểm mở rộng nổi bật, tạo nên sự khác biệt so với nhiều hệ thống quản lý nhà hàng truyền thống.

Về mặt học thuật, đề tài là sự kết hợp của nhiều lĩnh vực công nghệ hiện đại như phát triển ứng dụng doanh nghiệp, lập trình thời gian thực, bảo mật web, trí tuệ nhân tạo ứng dụng và Internet of Things trong cùng một hệ thống thống nhất.

## 4.3 Hạn chế của hệ thống

Mặc dù đã đạt được nhiều kết quả tích cực, hệ thống vẫn tồn tại một số hạn chế nhất định. Tính năng gọi món bằng giọng nói hiện còn phụ thuộc vào chất lượng môi trường âm thanh và độ chính xác của mô hình AI nhận diện ngôn ngữ, do đó hiệu quả sử dụng có thể bị ảnh hưởng trong môi trường nhà hàng đông người hoặc nhiều tiếng ồn.

Ngoài ra, hệ thống hiện được thiết kế chủ yếu cho mô hình nhà hàng đơn chi nhánh và chưa hỗ trợ quản lý đa cơ sở hoặc chuỗi nhà hàng. Một số chức năng nâng cao như phân tích dữ liệu chuyên sâu, gợi ý món ăn thông minh hoặc tích hợp loyalty program cho khách hàng cũng chưa được triển khai trong phạm vi đề tài.

Bên cạnh đó, quá trình kiểm thử hệ thống mới chủ yếu thực hiện trong môi trường giả lập và quy mô nhỏ, chưa có điều kiện đánh giá đầy đủ hiệu năng khi triển khai trên số lượng lớn người dùng đồng thời.

## 4.4 Hướng phát triển trong tương lai

Trong tương lai, hệ thống có thể tiếp tục được mở rộng và hoàn thiện theo nhiều hướng nhằm nâng cao khả năng ứng dụng thực tế. Trước tiên, cần cải thiện mô hình xử lý giọng nói và khả năng chống nhiễu của thiết bị IoT để tăng độ chính xác cho chức năng gọi món bằng giọng nói.

Bên cạnh đó, hệ thống có thể mở rộng hỗ trợ mô hình đa chi nhánh, cho phép quản lý tập trung nhiều nhà hàng trên cùng một nền tảng. Việc tích hợp thêm dashboard phân tích dữ liệu nâng cao, báo cáo doanh thu trực quan và dự đoán xu hướng tiêu dùng cũng là hướng phát triển tiềm năng nhằm hỗ trợ quản trị kinh doanh hiệu quả hơn.

Ngoài ra, đề tài có thể được phát triển thêm ứng dụng di động dành riêng cho khách hàng và quản lý, tích hợp chương trình khách hàng thân thiết, gợi ý món ăn thông minh dựa trên hành vi người dùng và hỗ trợ đa ngôn ngữ nhằm nâng cao trải nghiệm sử dụng.

## 4.5 Kiến nghị

Để hệ thống có thể triển khai hiệu quả trong thực tế, cần thực hiện thêm các giai đoạn kiểm thử và đánh giá trên môi trường nhà hàng thật với số lượng người dùng lớn hơn nhằm xác định giới hạn hiệu năng và độ ổn định của hệ thống. Đồng thời, cần nghiên cứu tối ưu hạ tầng triển khai, bảo mật mạng và khả năng mở rộng hệ thống để đáp ứng yêu cầu vận hành thực tế.

Ngoài ra, việc tiếp tục hoàn thiện các tính năng mở rộng và tối ưu trải nghiệm người dùng sẽ giúp hệ thống trở thành một giải pháp chuyển đổi số toàn diện hơn cho ngành dịch vụ nhà hàng trong tương lai.