package ro.itschool.Booking.convertorDTO;

import org.springframework.stereotype.Service;
import ro.itschool.Booking.Dto.PropertyDTO;
import ro.itschool.Booking.entity.Property;

import java.util.List;

@Service
public class PropertyConvertor {


    public PropertyDTO entityToDto(Property property) {
        //convert entity into DTO
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId(property.getId());
        propertyDTO.setPropertyName(property.getPropertyName());
        propertyDTO.setPropertyAddress(property.getPropertyAddress());
        propertyDTO.setPropertyLocation(property.getPropertyLocation());
        propertyDTO.setPropertyEmail(property.getPropertyEmail());

        return propertyDTO;

    }

    public List<PropertyDTO> entityToDto(List<Property> properties) {
        //convert a list of entity to DTO
        return properties.stream().map(x -> entityToDto(x)).toList();
    }

    public Property dtoToEntity(PropertyDTO propertyDTO) {
        Property property = new Property();
        property.setId(propertyDTO.getId());
        property.setPropertyType(propertyDTO.getPropertyType());
        property.setPropertyName(propertyDTO.getPropertyName());
        property.setPropertyEmail(propertyDTO.getPropertyEmail());
        property.setPropertyAddress(propertyDTO.getPropertyAddress());
        property.setPropertyLocation(propertyDTO.getPropertyLocation());

        return property;
    }

    public List<Property> dtoToEntity(List<PropertyDTO> propertyDTOS) {
        return propertyDTOS.stream().map(x -> dtoToEntity(x)).toList();
    }
}
