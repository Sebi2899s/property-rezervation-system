package ro.itschool.Booking.service;

import jakarta.annotation.Nullable;
import jakarta.el.PropertyNotFoundException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.Dto.PropertyDTO;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.entity.Reservation;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.specifications.PropertyTypeAndNameRequest;
import ro.itschool.Booking.specifications.QuerySpecificationsDao;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Service
@Service
public class PropertyService {
    //dependency injection
    private final PropertyRepository propertyRepository;
    private final QuerySpecificationsDao querySpecificationsDao;


    public PropertyService(PropertyRepository propertyRepository, QuerySpecificationsDao querySpecificationsDao) {
        this.propertyRepository = propertyRepository;
        this.querySpecificationsDao = querySpecificationsDao;
    }


    //---------------------------------------------------------------------------------------------------------------------
    public List<Property> getPropertiesByNameAndSortedAlphabetically(String name) {
        List<Property> properties = propertyRepository.getPropertyNameAndFilter(name).orElseThrow(() -> new RuntimeException("There no properties with this name"));
        properties.sort(Comparator.comparing(Property::getPropertyName));
        return properties;
    }

    //-------------------------------------------------------------------------------------------------------------------
//get property by name without sorting
    public List<Property> getPropertyByNameFilter(String name) {
        List<Property> properties = propertyRepository.getPropertyByName(name);
        if (properties.isEmpty() || properties == null) {
            return new ArrayList<>();
        }
        return properties;
    }


    //---------------------------------------------------------------------------------------------------------------------
    //GET
    public List<Property> getAllProperties(Integer pageNo,
                                           Integer pageSize,
                                           String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Property> pagedResult = propertyRepository.findAll(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }


    //---------------------------------------------------------------------------------------------------------------------
    //GET by property name
    public List<Property> getByPropertyName(String propertyName) throws IncorretNameException {
        if (propertyName == null) {
            throw new IncorretNameException("There's no property with this name " + propertyName);
        } else
            return propertyRepository.findByPropertyName(propertyName);
    }


    //---------------------------------------------------------------------------------------------------------------------
    public Property getPropertyOrThrow(Long id) {
        Optional<Property> property = findById(id);
        return property.orElseThrow(() -> new PropertyNotFoundException("Property with this id" + id + "was not found!"));
    }


    //---------------------------------------------------------------------------------------------------------------------

    public List<Property> getPropertiesByType(String propertyType) {
        List<Property> allByPropertyType = propertyRepository.findAllByPropertyType(propertyType);
        return allByPropertyType;
    }


    //---------------------------------------------------------------------------------------------------------------------
    //excel file report of all persons
    public void generateExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<Property> propertyList = propertyRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("List With Properties");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Property Name");
        row.createCell(2).setCellValue("Property Email");
        row.createCell(3).setCellValue("Property Address");
        row.createCell(4).setCellValue("Price");
        row.createCell(5).setCellValue("Description");

        int dataRowIndex = 1;
        for (Property property : propertyList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(property.getId());
            dataRow.createCell(1).setCellValue(property.getPropertyName());
            dataRow.createCell(2).setCellValue(property.getPropertyEmail());
            dataRow.createCell(3).setCellValue(property.getPropertyAddress());
            dataRow.createCell(4).setCellValue(property.getPrice());
            dataRow.createCell(5).setCellValue(property.getDescription());
            dataRowIndex++;
        }
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }


    //---------------------------------------------------------------------------------------------------------------------
    //POST
    public Property createProperty(Property property_p) {
        Property property = null;
        if (property_p == null) {
            propertyEmailExistsCheck(property_p);
            property = property_p;
        } else {
            property = property_p;
        }
        return propertyRepository.save(property);
    }


    //---------------------------------------------------------------------------------------------------------------------
    public Property updateOrSaveProperty(@NotNull Property propertyRequest, @Nullable Long propertyId) throws IncorrectIdException {
        if (propertyId == null) {
            return createProperty(propertyRequest);
        } else {
            Property property = updateProperty(propertyId, propertyRequest);
            return createProperty(property);
        }
    }


    //---------------------------------------------------------------------------------------------------------------------
    //UPDATE
    public Property updateProperty(Long id, Property propertyRequest) throws IncorrectIdException {
        Property propertyUpdate = propertyRepository.findById(id).orElseThrow(() -> new IncorrectIdException("This id" + id + "is not found! "));
        propertyEmailExistsCheck(propertyRequest);

        propertyUpdate.setPropertyName(propertyRequest.getPropertyName());
        propertyUpdate.setPropertyLocation(propertyRequest.getPropertyLocation());
        propertyUpdate.setPropertyAddress(propertyRequest.getPropertyAddress());
        propertyUpdate.setPropertyType(propertyRequest.getPropertyType());
        propertyUpdate.setDescription(propertyRequest.getDescription());
        propertyUpdate.setPropertyLocation(propertyRequest.getPropertyLocation());
        propertyUpdate.setPrice(propertyRequest.getPrice());
        propertyUpdate.setPersonList(propertyRequest.getPersonList());
        propertyUpdate.setReservations(propertyRequest.getReservations());
        return propertyUpdate;
    }


    //---------------------------------------------------------------------------------------------------------------------
    //DELETE
    public void deleteProperty(Long id) {
        Optional<Property> person = propertyRepository.findById(id);
        if (person.isEmpty())
            throw new PropertyNotFoundException("This property with id " + id + " was not found!");
        else {
            propertyRepository.deleteById(id);
        }
    }
//---------------------------------------------------------------------------------------------------------------------

    public List<Property> getPropertiesByTypeAndName(PropertyTypeAndNameRequest propertyTypeAndNameRequest) {
        return querySpecificationsDao.getAllPropertyByTypeAndName(propertyTypeAndNameRequest);
    }


    //---------------------------------------------------------------------------------------------------------------------
    public Optional<PropertyDTO> checkIfIdExistsConvertToDto(Long id) {
        Optional<Property> idExists = propertyRepository.findById(id);
        if (idExists.isEmpty()) {
            throw new IllegalStateException("This property with id: " + id + " is not found");
        }
        return idExists.map(Property::toDTO);
    }


    //---------------------------------------------------------------------------------------------------------------------
    public Optional<Property> findByPropertyEmail(String propertyEmail) {
        return propertyRepository.findByPropertyEmail(propertyEmail);
    }


    //---------------------------------------------------------------------------------------------------------------------
    public Optional<Property> findById(Long id) {
        return propertyRepository.findById(id);
    }

    //---------------------------------------------------------------------------------------------------------------------

    public static Specification<Property> getPropertyByPropertyType(String propertyType) {
        Specification<Property> propertySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("propertyType"), propertyType);
        return propertySpecification;
    }

    public boolean canReserve(Reservation reservation) {
        List<LocalDate> existingBlockedDates = reservation.getProperty().getBlockedDates();
        List<LocalDate> datesBetweenCheckInAndCheckOut = getDatesBetween(reservation.getCheckInDate(), reservation.getCheckOutDate());
        if (existingBlockedDates.stream().anyMatch(datesBetweenCheckInAndCheckOut::contains)) {
            return false;
        } else {
            return true;
        }
    }

    private static List<LocalDate> getDatesBetween(LocalDate checkIn, LocalDate CheckOut) {
        return Stream.iterate(checkIn, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(checkIn, CheckOut))
                .collect(Collectors.toList());
    }

    //method that check if email exists
    private void propertyEmailExistsCheck(Property property) {
        Optional<Property> propertyOptional = propertyRepository.getPropertyByPropertyEmail(property.getPropertyEmail());
        if (propertyOptional.isPresent()) {
            throw new IllegalStateException(String.format("This email %s already exists", property.getPropertyEmail()));
        }
    }

}
