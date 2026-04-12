package restaurant.project.order_table.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import restaurant.project.order_table.service.ExcelService;
import restaurant.project.order_table.util.ExcelUtil;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExcelController {

    private final ExcelService excelService;

    @PostMapping("/import/{entity}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> importExcel(@PathVariable String entity, @RequestParam("file") MultipartFile file) {
        if (!ExcelUtil.hasExcelFormat(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vui lòng tải lên file Excel (.xlsx)");
        }
        try {
            excelService.importData(entity, file);
            return ResponseEntity.ok("Import dữ liệu cho [" + entity + "] thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Không thể import: " + e.getMessage());
        }
    }

    @GetMapping("/export/{entity}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> exportExcel(@PathVariable String entity) {
        String filename = entity + "s.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.exportData(entity));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
