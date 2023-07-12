package ro.itschool.Booking.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.itschool.Booking.DtoEntity.PersonDTO;
import ro.itschool.Booking.DtoEntity.PropertyDTO;
import ro.itschool.Booking.convertorDTO.PropertyConvertor;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.service.PropertyService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/property")
public class PropertyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyController.class);
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PropertyRepository propertyRepository;


    @GetMapping(value = "/get-all")
    public List<PropertyDTO> propertyList(@RequestParam(defaultValue = "0") Integer pageNo,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(defaultValue = "ASC") String sortBy) {
        LOGGER.info("Getting all properties");
        return propertyService.getAllProperties(pageNo, pageSize, sortBy).stream().map(Property::toDTO).toList();
    }


//---------------------------------------------------------------------------------------------------------------------

    //get reservation by name
    @GetMapping(value = "/name")
    public ResponseEntity<List<PropertyDTO>> properties(@RequestParam String name) {
        LOGGER.info("Getting property by name");
        return new ResponseEntity<>(propertyService.getPropertiesByNameAndSortedAlphabetically(name).stream().map(Property::toDTO).toList(), HttpStatus.OK);
    }


//---------------------------------------------------------------------------------------------------------------------

    @GetMapping(value = "/type")
    public ResponseEntity<List<PropertyDTO>> propertyByPropertyType(@RequestParam String propertyType) {
        List<Property> propertyByPropertyType = propertyService.getPropertyByPropertyType(propertyType);
        return new ResponseEntity<>(propertyByPropertyType.stream().map(Property::toDTO).toList(), HttpStatus.OK);
    }


//---------------------------------------------------------------------------------------------------------------------

    @GetMapping(value = "/person-first-name")
    public ResponseEntity<List<PropertyDTO>> propertyByCustomerFirstName(@RequestParam String firstName) {
        List<Property> propertyByPersonFirstName = propertyService.getPropertyByPersonFirstName(firstName);
        return new ResponseEntity<>(propertyByPersonFirstName.stream().map(Property::toDTO).toList(), HttpStatus.OK);
    }


    //---------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = "/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {


        response.setContentType("application");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=property.xls";

        response.setHeader(headerKey, headerValue);
        propertyService.generateExcel(response);
    }


    //---------------------------------------------------------------------------------------------------------------------
    @PostMapping(value = "/save")
    public ResponseEntity<PropertyDTO> saveProperty(@RequestBody Property property) {
        LOGGER.info("Saving a property");
        PropertyConvertor propertyConvertor = new PropertyConvertor();
        propertyService.createOrUpdateProperty(property, null);
        return new ResponseEntity<>(propertyConvertor.entityToDto(property), HttpStatus.OK);
    }

    //---------------------------------------------------------------------------------------------------------------------
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, @RequestBody Property property) {
        LOGGER.info("Updating a property using the id value");
        propertyService.createOrUpdateProperty(property, id);
        PropertyConvertor propertyConvertor = new PropertyConvertor();
        return new ResponseEntity<>(propertyConvertor.entityToDto(property), HttpStatus.OK);
    }

    //---------------------------------------------------------------------------------------------------------------------
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Long> deleteProperty(@PathVariable Long id) {
        LOGGER.info("Deleting a property using the id value");
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    //---------------------------------------------------------------------------------------------------------------------
    @GetMapping("/property/{id}")
    public ResponseEntity<Optional<PropertyDTO>> getPropertyById(@PathVariable Long id) {
        return new ResponseEntity<>(propertyService.checkIfIdExistsConvertToDto(id), HttpStatus.OK);

    }

}
