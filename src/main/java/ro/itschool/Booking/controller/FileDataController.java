package ro.itschool.Booking.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.FileData;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.service.GeneratePdfInvoiceService;
import ro.itschool.Booking.service.ReservationService;
import ro.itschool.Booking.service.StorageService;
import ro.itschool.Booking.util.FileDataUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping(value = "/file")
public class FileDataController {

    @Autowired
    private StorageService service;
    @Autowired
    private GeneratePdfInvoiceService generatePdfInvoiceService;
    @Autowired
    private ReservationService reservationService;

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadFile = service.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadFile);
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<FileData> result = service.findById(id);
        if (!result.isPresent()) {
            throw new RuntimeException("Couldn't find the file with id " + id);
        }
        byte[] decompressFileFromDb = FileDataUtils.decompressFile(result.get().getFileData());
        FileData fileData = result.get();
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename" + fileData.getName();

        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(fileData.getFileData());
        outputStream.close();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(decompressFileFromDb);
    }


    //download with csv
    @GetMapping("/download/csv")
    public void downloadCSV(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.csv\"");

        List<FileData> dataList = service.findAll();

        StatefulBeanToCsv<FileData> writer = new StatefulBeanToCsvBuilder<FileData>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(dataList);
    }

    @GetMapping("/invoice/{id}")
    public void downloadReservationPdf(HttpServletResponse response,@PathVariable Long id) throws IOException, IncorrectIdException {

        Reservation reservationById = reservationService.getReservationById(id).get();
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";

        this.generatePdfInvoiceService.export(response,reservationById);


    }

}
