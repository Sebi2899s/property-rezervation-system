package ro.itschool.Booking.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.itschool.Booking.entity.FileData;
import ro.itschool.Booking.service.StorageService;
import ro.itschool.Booking.util.FileDataUtils;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/file")
public class FileDataController {

    @Autowired
    private StorageService service;

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadFile = service.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadFile);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<?> downloadFile(@Param("id")Long id, HttpServletResponse response) throws IOException {
        Optional<FileData> result = service.findById(id);
        if (!result.isPresent()){
            throw new RuntimeException("Couldn't find the file with id "+ id);
        }
        byte[] decompressFileFromDb = FileDataUtils.decompressFile(result.get().getFileData());
        FileData fileData = result.get();
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        String headerKey="Content-Disposition";
        String headerValue = "attachment; filename" + fileData.getName();

        response.setHeader(headerKey,headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(fileData.getFileData());
        outputStream.close();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(decompressFileFromDb);
    }
}
