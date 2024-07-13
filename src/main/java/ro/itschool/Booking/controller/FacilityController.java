package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.FacilityRq;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Facility;
import ro.itschool.Booking.service.FacilityService;

import java.util.List;

@RestController
@RequestMapping("/api/facility")
public class FacilityController {
    @Autowired
    private FacilityService facilityService;


    @GetMapping(value = "/all")
    public List<Facility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping(value = "/facility/{id}")
    public Facility getFacility(@PathVariable Long id) throws IncorrectIdException {
        return facilityService.getFacilityById(id);
    }

    @PostMapping(value = "/save")
    public Facility saveFacility(@RequestBody FacilityRq facilityRq) throws IncorrectIdException {
        return facilityService.saveFacility(facilityRq);

    }

    @PutMapping(value = "/update/{id}")
    public Facility updateFacility(@PathVariable Long id, @RequestBody FacilityRq facilityRq) throws IncorrectIdException {
        return facilityService.updateFacility(id, facilityRq);

    }

    @DeleteMapping(value = "/delete/{id}")
    public Long deleteFacility(@PathVariable Long id) throws IncorrectIdException {
        return facilityService.deleteFacility(id);
    }
}
