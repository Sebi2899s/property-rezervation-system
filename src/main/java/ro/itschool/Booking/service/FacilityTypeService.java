package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.FacilityType;
import ro.itschool.Booking.repository.FacilityTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacilityTypeService {
    @Autowired
    private FacilityTypeRepository facilityTypeRepository;

    public List<FacilityType> getAllFacilitiesType() {
        List<FacilityType> facilityTypeList = facilityTypeRepository.findAll();
        return facilityTypeList.isEmpty() ? new ArrayList<>() : facilityTypeList;
    }

    public FacilityType getFacilityTypeById(Long id) throws IncorrectIdException {
        FacilityType facilityType = facilityTypeRepository.findById(id).orElseThrow(() -> new IncorrectIdException("This id" + id + " doesn't find any facility !"));
        return facilityType;
    }

    public FacilityType saveFacilityType(FacilityType facilityType) {
        return facilityTypeRepository.save(facilityType);

    }

    public FacilityType updateFacilityType(Long facilityTypeId, FacilityType facilityTypeRq) throws IncorrectIdException {
        FacilityType facilityType = getFacilityTypeById(facilityTypeId);
        facilityType.setName(facilityTypeRq.getName());
        return facilityTypeRepository.save(facilityType);

    }

    public Long deleteFacilityType(Long facilityTypeId) throws IncorrectIdException {
        FacilityType facilityTypeById = getFacilityTypeById(facilityTypeId);
        if (facilityTypeId != null && facilityTypeById != null) {
            facilityTypeRepository.deleteById(facilityTypeId);
        }
        return facilityTypeId;
    }
}
