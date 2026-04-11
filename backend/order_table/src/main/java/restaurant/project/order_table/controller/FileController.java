package restaurant.project.order_table.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import restaurant.project.order_table.dto.response.ApiResponse;
import restaurant.project.order_table.service.FileService;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filename = fileService.saveFile(file);
            String fileUrl = "/uploads/" + filename;
            return ApiResponse.success(fileUrl, "Tải lên file thành công");
        } catch (IOException e) {
            return ApiResponse.error("Lỗi khi tải lên file: " + e.getMessage());
        }
    }
}
