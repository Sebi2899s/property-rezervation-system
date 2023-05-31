package ro.itschool.Booking.service;

import jakarta.annotation.Nullable;
import jakarta.el.PropertyNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.customException.IncorretNameException;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.specifications.Specifications;

import java.util.*;

//@Service
@Service
public class PropertyService {
    //dependency injection
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }


    public List<Property> getPropertiesByNameAndSortedAlphabetically(String name) {
        List<Property> properties = propertyRepository.getPropertyNameAndFilter(name).orElseThrow(() -> new RuntimeException("There no properties with this name"));
        properties.sort(Comparator.comparing(Property::getPropertyName));
        return properties;
    }

    //GET
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    //GET by property name
    public List<Property> getByPropertyName(String propertyName) throws IncorretNameException {
        if (propertyName == null) {
            throw new IncorretNameException("There's no property with this name " + propertyName);
        } else
            return propertyRepository.findByPropertyName(propertyName);
    }

    public Property getPropertyOrThrow(Long id) {
        Optional<Property> property = findById(id);
        return property.orElseThrow(() -> new PropertyNotFoundException("Property with this id" + id + "was not found!"));
    }

    //get property by property type using query specification
    public List<Property> getPropertyByPropertyType(String propertyTipe) {
        Specification<Property> propertySpecification = Specifications.getPropertyByPropertyType(propertyTipe);
        return propertyRepository.findAll(propertySpecification);
    }

    //get property where person have the following first name
    public List<Property> getPropertyByPersonFirstName(String firstName) {
        Specification<Property> specifications = Specifications.getPropertyByPersonFirstName(firstName);
        return propertyRepository.findAll(specifications);
    }

    //POST
    public Property createProperty(Property property) {
        Property save = null;
        if (property == null) {
            propertyEmailExistsCheck(property);
        } else {
            save = propertyRepository.save(property);
        }
        return save;
    }

    public Property createOrUpdateProperty(@NotNull Property property_p, @Nullable Long id) {
        Property property;
        String sMessage = null;
        //update case
        if (id != null) {
            property = getPropertyOrThrow(id);
            sMessage = "Error in updating property";
        } else {
            property_p.setId(null);
            sMessage = "Error in saving property";
        }
        try {

            property = createProperty(property_p);
        } catch (Exception e) {
            throw new RuntimeException(sMessage);
        }
        return property;
    }


    //method that check if email exists
    private void propertyEmailExistsCheck(Property property) {
        Optional<Property> propertyOptional = propertyRepository.getPropertyByPropertyEmail(property.getPropertyEmail());
        if (propertyOptional.isPresent()) {
            throw new IllegalStateException(String.format("This email %s already exists", property.getPropertyEmail()));
        }
    }

    //UPDATE
    public void updateProperty(Long id, Property property) throws IncorrectIdException {
        Property propertyUpdate = propertyRepository.findById(id).orElseThrow(() -> new IncorrectIdException("This id" + property.getId() + "is not found! "));
        propertyEmailExistsCheck(property);

        propertyUpdate.setId(property.getId());
        propertyUpdate.setPropertyName(property.getPropertyName());
        propertyUpdate.setPropertyEmail(property.getPropertyEmail());
        propertyUpdate.setPropertyLocation(property.getPropertyLocation());
        propertyUpdate.setPropertyAddress(property.getPropertyAddress());
        propertyUpdate.setPropertyType(property.getPropertyType());
    }

    //DELETE
    public void deleteProperty(Long id) {
        Optional<Property> person = propertyRepository.findById(id);
        if (person.isEmpty())
            throw new PropertyNotFoundException("This property with id " + id + " was not found!");
        else {
            propertyRepository.deleteById(id);
        }
    }

    public Optional<Property> findByPropertyEmail(String propertyEmail) {
        return propertyRepository.findByPropertyEmail(propertyEmail);
    }

    public Optional<Property> findById(Long id) {
        return propertyRepository.findById(id);
    }
}
