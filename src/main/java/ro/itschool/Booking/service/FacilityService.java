package ro.itschool.Booking.service;

import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.Dto.FacilityRq;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Facility;
import ro.itschool.Booking.entity.FacilityType;
import ro.itschool.Booking.repository.FacilityRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class FacilityService {
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private FacilityTypeService facilityTypeService;

    public List<Facility> getAllFacilities() {
        List<Facility> allFacilities = facilityRepository.findAll();
        return allFacilities.isEmpty() ? new ArrayList<>() : allFacilities;
    }
    public Facility getFacilityById(Long id) throws IncorrectIdException {
        Facility facility = facilityRepository.findById(id).orElseThrow(() -> new IncorrectIdException("This id" + id + " doesn't find any facility !"));
        return facility;
    }

    public Facility saveFacility(FacilityRq facilityRq) throws IncorrectIdException {
        String name = facilityRq.getName();
        String description = facilityRq.getDescription();
        FacilityType facilityType = facilityTypeService.getFacilityTypeById(facilityRq.getFacilityTypeId());
        Facility facility= Facility.builder().name(name).description(description).facilityType(facilityType).build();
        return facilityRepository.save(facility);
    }
    public Facility updateFacility(@NonNull Long facilityId, FacilityRq facilityRq) throws IncorrectIdException {
        Facility facility = getFacilityById(facilityId);
        facility.setName(facilityRq.getName());
        facility.setDescription(facilityRq.getDescription());
        FacilityType facilityType = facilityTypeService.getFacilityTypeById(facilityRq.getFacilityTypeId());
        facility.setFacilityType(facilityType);
        return facilityRepository.save(facility);
    }

    public Long deleteFacility(Long facilityId) throws IncorrectIdException {
        Facility facilityById = getFacilityById(facilityId);
        if (facilityId!= null && facilityById != null){
            facilityRepository.deleteById(facilityId);
        }
        return facilityId;
    }

}
