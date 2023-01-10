package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.DtoEntity.PropertyDTO;
import ro.itschool.Booking.entity.Person;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.service.PropertyService;

import java.util.List;

@RestController
@RequestMapping(value = "/bk/property")
public class PropertyController {
    //dependency injection with constructor
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Autowired
    private PropertyRepository propertyRepository;


    @GetMapping(value = "/get-all")
    public List<PropertyDTO> propertyList() {
        return propertyService.getAllProperties().stream().map(Property::toDTO).toList();
    }


    @GetMapping(value = "/rezervation-by-name")
    public List<PropertyDTO> properties(@RequestParam String name) {

        return propertyService.getByPropertyName(name).stream().map(Property::toDTO).toList();
    }


    @PostMapping(value = "/save-property")
    public Property saveProperty(@RequestBody Property property) {
        return propertyService.createProperty(property);
    }



    @PutMapping(value = "/update-property/{id}")
    public void updateProperty(@PathVariable Long id, @RequestBody Property property) {
        propertyService.updateProperty(id, property);
    }

    @DeleteMapping(value = "/delete-property/{id}")
    public void deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }


}
