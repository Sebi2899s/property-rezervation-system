package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.itschool.Booking.entity.FileData;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<FileData,Long> {
    @Query(value = "SELECT file_data.name FROM file_data WHERE file_data.name= :fileName", nativeQuery = true)
    Optional<FileData> getNameFromFileDataDb(String fileName);
}
