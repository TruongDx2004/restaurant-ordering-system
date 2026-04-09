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
        System.out.println("Received file upload: " + file.getOriginalFilename() + " (" + file.getSize() + " bytes)");
        try {
            String filename = fileService.saveFile(file);
            String fileUrl = "/uploads/" + filename;
            System.out.println("File saved successfully as: " + filename);
            return ApiResponse.success(fileUrl, "File uploaded successfully");
        } catch (IOException e) {
            System.err.println("File upload failed: " + e.getMessage());
            return ApiResponse.error("Failed to upload file: " + e.getMessage());
        }
    }
}
