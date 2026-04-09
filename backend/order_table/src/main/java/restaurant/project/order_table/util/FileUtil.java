package restaurant.project.order_table.util;

import java.nio.file.*;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String saveFile(MultipartFile file) {
        try {
            String baseDir = System.getProperty("user.dir");
            String uploadDir = baseDir + "/uploads/";

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            System.out.println("Saved at: " + path.toAbsolutePath());

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Upload file failed");
        }
    }
}
