package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.itschool.Booking.entity.FileData;
import ro.itschool.Booking.repository.StorageRepository;
import ro.itschool.Booking.util.FileDataUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    public String uploadFile(MultipartFile file) throws IOException {
        FileData fileData = storageRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .fileData(FileDataUtils.compressFile(file.getBytes()))
                .uploadTime(LocalDateTime.now())
                .build());
        if (fileData != null) {
            return FileDataUtils.FILE_UPLOADED_SUCCESSFULLY + " " + file.getOriginalFilename();
        }
        return FileDataUtils.FILE_UPLOADED_ERROR + " " + file.getOriginalFilename();
    }

    public byte[] downloadFile(String fileName) {
        Optional<FileData> dbFileData = storageRepository.getNameFromFileDataDb(fileName);
        byte[] byteFile = FileDataUtils.decompressFile(dbFileData.get().getFileData());
        return byteFile;
    }

    public Optional<FileData> findById(Long id) {
        Optional<FileData> getFileById = storageRepository.findById(id);
        FileData o = getFileById.isEmpty() ? null : getFileById.get();
        return getFileById;
    }

    public List<FileData> findAll() {
        return storageRepository.findAll();
    }

    public FileData saveFileData(FileData fileData){
        return storageRepository.save(fileData);
    }
    public List<FileData> saveAllFiles(List<FileData> files){
        return storageRepository.saveAll(files);
    }
}
