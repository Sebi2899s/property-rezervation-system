package ro.itschool.Booking.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.PropertyDTO;
import ro.itschool.Booking.convertorDTO.PropertyConvertor;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.entity.Status;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.service.PropertyService;

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
    public ResponseEntity<List<Property>> propertyByPropertyType(@RequestParam String propertyType) {
        List<Property> propertyByPropertyType = propertyService.getPropertiesByType(propertyType);
        return new ResponseEntity<>(propertyByPropertyType, HttpStatus.OK);
    }


//---------------------------------------------------------------------------------------------------------------------



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
    public Status saveProperty(@RequestBody Property propertyRq) {
        LOGGER.info("Saving a property");
        Status status = new Status();
        PropertyConvertor propertyConvertor = new PropertyConvertor();
        Property property = propertyService.updateOrSaveProperty(propertyRq, null);

        status.setMessage("Property added successfully !");
        status.setId(property.getId());

        return status;
    }

    //---------------------------------------------------------------------------------------------------------------------
    @PutMapping(value = "/update/{id}")
    public Status updateProperty(@PathVariable Long id, @RequestBody Property propertyRq) {
        LOGGER.info("Updating a property using the id value");
        Status status = new Status();
        Property property = propertyService.updateOrSaveProperty(propertyRq, id);

        status.setMessage("Property updated successfully !");
        status.setId(property.getId());

        return status;
    }

    //---------------------------------------------------------------------------------------------------------------------
    @DeleteMapping(value = "/delete/{id}")
    public Status deleteProperty(@PathVariable Long id) {
        LOGGER.info("Deleting a property using the id value");
        Status status = new Status();
        propertyService.deleteProperty(id);

        status.setMessage("Property added successfully !");
        status.setId(id);

        return status;
    }

    //---------------------------------------------------------------------------------------------------------------------
    @GetMapping("/property/{id}")
    public ResponseEntity<Optional<PropertyDTO>> getPropertyById(@PathVariable Long id) {
        return new ResponseEntity<>(propertyService.checkIfIdExistsConvertToDto(id), HttpStatus.OK);

    }

}
