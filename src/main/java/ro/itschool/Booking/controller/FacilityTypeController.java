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

    @GetMapping(path = "/get-all")
    public List<FacilityType> facilityTypeList() {
        return facilityTypeService.getAllFacilitiesType();
    }

    @GetMapping()
    public FacilityType getFacilityTypeById (@RequestParam Long facilityTypeId) throws IncorrectIdException {
        return facilityTypeService.getFacilityTypeById(facilityTypeId);
    }

    @PostMapping(path = "/save")
    public FacilityType saveFacilityType(@RequestBody FacilityType facilityType){
        return facilityTypeService.saveFacilityType(facilityType);
    }

    @PutMapping(path = "/update")
    public FacilityType updateFacilityType(@RequestParam Long facilityTypeId, @RequestBody FacilityType facilityType) throws IncorrectIdException {
        return facilityTypeService.updateFacilityType(facilityTypeId,facilityType);
    }

    @DeleteMapping(path = "/delete")
    public Long deleteFacilityType(@RequestParam Long facilityTypeId) throws IncorrectIdException {
        return facilityTypeService.deleteFacilityType(facilityTypeId);
    }
}
