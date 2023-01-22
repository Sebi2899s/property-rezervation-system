package ro.itschool.Booking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.PropertyDTO;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.exception.IncorrectIdException;
import ro.itschool.Booking.exception.IncorretNameException;
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


    @GetMapping(value = "/rezervation-by-name")
    public List<PropertyDTO> properties(@RequestParam String name) throws IncorretNameException {
        LOGGER.info("Getting property by name");
        return propertyService.getByPropertyName(name).stream().map(Property::toDTO).toList();
    }


    @PostMapping(value = "/save-property")
    public Property saveProperty(@RequestBody Property property) {
        LOGGER.info("Saving a property");
        return propertyService.createProperty(property);
    }


    @PutMapping(value = "/update-property/{id}")
    public void updateProperty(@PathVariable Long id, @RequestBody Property property) throws IncorrectIdException {
        LOGGER.info("Updating a property using the id value");
        propertyService.updateProperty(id, property);
    }

    @DeleteMapping(value = "/delete-property/{id}")
    public void deleteProperty(@PathVariable Long id) throws IncorrectIdException {
        LOGGER.info("Deleting a property using the id value");

        propertyService.deleteProperty(id);
    }


}
