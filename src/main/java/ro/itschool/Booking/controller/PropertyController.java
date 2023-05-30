package ro.itschool.Booking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.PropertyDTO;
import ro.itschool.Booking.convertorDTO.PropertyConvertor;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.service.PropertyService;

import java.util.List;

@RestController
@RequestMapping(value = "/bk/property")
public class PropertyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyController.class);

    //dependency injection with constructor
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Autowired
    private PropertyRepository propertyRepository;


    @GetMapping(value = "/get-all")
    public List<PropertyDTO> propertyList() {
        LOGGER.info("Getting all properties");
        return propertyService.getAllProperties().stream().map(Property::toDTO).toList();
    }

//get reservation by name
    @GetMapping(value = "/get-reservation")
    public ResponseEntity<List<PropertyDTO>> properties(@RequestParam String name) throws IncorretNameException {
        LOGGER.info("Getting property by name");
        return new ResponseEntity<>(propertyService.getPropertiesByNameAndSortedAlphabetically(name).stream().map(Property::toDTO).toList(), HttpStatus.OK);
    }

    @GetMapping(value = "/filter-type")
    public ResponseEntity<List<PropertyDTO>> propertyByPropertyType(@RequestParam String propertyType) {
        List<Property> propertyByPropertyType = propertyService.getPropertyByPropertyType(propertyType);
        return new ResponseEntity<>(propertyByPropertyType.stream().map(Property::toDTO).toList(), HttpStatus.OK);
    }

    @GetMapping(value = "/filter-first-name")
    public ResponseEntity<List<PropertyDTO>> propertyByCustomerFirstName(@RequestParam String firstName) {
        List<Property> propertyByPersonFirstName = propertyService.getPropertyByPersonFirstName(firstName);
        return new ResponseEntity<>(propertyByPersonFirstName.stream().map(Property::toDTO).toList(), HttpStatus.OK);
    }

    @PostMapping(value = "/save-property")
    public ResponseEntity<PropertyDTO> saveProperty(@RequestBody Property property) {
        LOGGER.info("Saving a property");
        PropertyConvertor propertyConvertor = new PropertyConvertor();
        propertyService.createProperty(property);
        return new ResponseEntity<>(propertyConvertor.entityToDto(property), HttpStatus.OK);
    }


    @PutMapping(value = "/update-property/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, @RequestBody Property property) throws IncorrectIdException {
        LOGGER.info("Updating a property using the id value");
        propertyService.updateProperty(id, property);
        PropertyConvertor propertyConvertor = new PropertyConvertor();
        return new ResponseEntity<>(propertyConvertor.entityToDto(property), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-property/{id}")
    public ResponseEntity<Long> deleteProperty(@PathVariable Long id) throws IncorrectIdException {
        LOGGER.info("Deleting a property using the id value");
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


}
