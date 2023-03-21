package ro.itschool.Booking.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.exception.IncorrectIdException;
import ro.itschool.Booking.exception.IncorretNameException;
import ro.itschool.Booking.repository.PropertyRepository;
import ro.itschool.Booking.specifications.Specifications;

import java.util.List;
import java.util.Optional;

//@Service
@Service
public class PropertyService {
    //dependency injection
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
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
        propertyEmailExistsCheck(property);

        return propertyRepository.save(property);
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
    public void deleteProperty(Long id) throws IncorrectIdException {
        Optional<Property> checkId = propertyRepository.findById(id);
        if (checkId.isEmpty())
            throw new IncorrectIdException("This id" + id + "is not found!");
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
