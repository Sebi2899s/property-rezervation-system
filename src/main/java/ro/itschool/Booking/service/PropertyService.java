package ro.itschool.Booking.service;

import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Property;
import ro.itschool.Booking.repository.PropertyRepository;

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
    public List<Property> getByPropertyName(String propertyName) {
        if (propertyName == null) {
            throw new IllegalStateException("There's no property with this name " + propertyName);
        } else
            return propertyRepository.findByPropertyName(propertyName);
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
    public void updateProperty(Long id, Property property) {
        Property propertyUpdate = propertyRepository.findById(id).orElseThrow(() -> new IllegalStateException("This id" + property.getId() + "is not found! "));
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
        Optional<Property> checkId = propertyRepository.findById(id);
        if (checkId.isEmpty())
            throw new IllegalStateException("This id" + id + "is not found!");
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
