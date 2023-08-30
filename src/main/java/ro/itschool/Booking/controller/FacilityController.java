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


    @GetMapping(path = "/all")
    public List<Facility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping()
    public Facility getFacility(Long id) throws IncorrectIdException {
        return facilityService.getFacilityById(id);
    }

    @PostMapping(path = "/save")
    public Facility saveFacility(@RequestBody FacilityRq facilityRq) throws IncorrectIdException {
        return facilityService.saveFacility(facilityRq);

    }

    @PutMapping(path = "/update")
    public Facility updateFacility(@RequestParam Long facilityId, @RequestBody FacilityRq facilityRq) throws IncorrectIdException {
        return facilityService.updateFacility(facilityId, facilityRq);

    }

    @DeleteMapping(path = "/delete")
    public Long deleteFacility(@RequestParam Long facilityId) throws IncorrectIdException {
        return facilityService.deleteFacility(facilityId);
    }
}
