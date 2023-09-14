package ro.itschool.Booking.controller;

import org.apache.commons.collections4.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.FacilityType;
import ro.itschool.Booking.service.FacilityTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/facilityType")
public class FacilityTypeController {
    @Autowired
    private FacilityTypeService facilityTypeService;

    @GetMapping(value = "/get-all")
    public List<FacilityType> facilityTypeList() {
        return facilityTypeService.getAllFacilitiesType();
    }

    @GetMapping(value = "/facility-type/{id}")
    public FacilityType getFacilityTypeById (@PathVariable Long id) throws IncorrectIdException {
        return facilityTypeService.getFacilityTypeById(id);
    }

    @PostMapping(value = "/save")
    public FacilityType saveFacilityType(@RequestBody FacilityType facilityType){
        return facilityTypeService.saveFacilityType(facilityType);
    }

    @PutMapping(value = "/update/{id}")
    public FacilityType updateFacilityType(@PathVariable Long id, @RequestBody FacilityType facilityType) throws IncorrectIdException {
        return facilityTypeService.updateFacilityType(id,facilityType);
    }

    @DeleteMapping(value = "/delete/{id}")
    public Long deleteFacilityType(@PathVariable Long id) throws IncorrectIdException {
        return facilityTypeService.deleteFacilityType(id);
    }
}
