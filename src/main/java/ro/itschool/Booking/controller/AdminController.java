package ro.itschool.Booking.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import ro.itschool.Booking.Dto.CouponRequestDTO;
import ro.itschool.Booking.Dto.FacilityRq;
import ro.itschool.Booking.Dto.ReservationRequestDTO;
import ro.itschool.Booking.customException.*;
import ro.itschool.Booking.entity.*;
import ro.itschool.Booking.service.*;

import java.util.List;
import java.util.Optional;

@RestController
@EnableWebSecurity
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    //pull request
    private final PersonService personService;
    private final PropertyService propertyService;
    private final ReservationService reservationService;
    private final FacilityService facilityService;
    private final CouponService couponService;
    private final FacilityTypeService facilityTypeService;
    private final ReminderService reminderService;

    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Person>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(personService.getAllPersons(pageNo, pageSize, sortBy), HttpStatus.OK);
    }

    @GetMapping(value = "/properties")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Property>> getAllProperties(@RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam(defaultValue = "ASC") String sortBy) {
        return new ResponseEntity<>(propertyService.getAllProperties(pageNo, pageSize, sortBy), HttpStatus.OK);
    }

    @GetMapping(value = "/reservations")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                                @RequestParam(defaultValue = "ASC") String sortBy) {
        return new ResponseEntity<>(reservationService.getAllReservationForAdmin(pageNo, pageSize, sortBy), HttpStatus.OK);
    }

    @GetMapping(value = "/properties-filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Property>> getPropertiesByName(@RequestParam String name) {
        return new ResponseEntity<>(propertyService.getPropertyByNameFilter(name), HttpStatus.OK);
    }

    @GetMapping(value = "/persons-filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Person>> getPersonsByFirstName(@RequestParam String name) {
        return new ResponseEntity<>(personService.getPropertyByNameFilter(name), HttpStatus.OK);
    }

    @GetMapping(value = "/coupon/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Coupon getCouponById(@PathVariable Long id) throws Exception {
        return couponService.getCoupon(id);
    }

    @GetMapping(value = "/all/coupons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }
    @GetMapping(value = "/all-facility")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Facility> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping(value = "/facility/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Facility getFacility(Long id) throws IncorrectIdException {
        return facilityService.getFacilityById(id);
    }

    @GetMapping(value = "/all/facility-type")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<FacilityType> facilityTypeList() {
        return facilityTypeService.getAllFacilitiesType();
    }

    @GetMapping(value = "/facility-type/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public FacilityType getFacilityTypeById (@PathVariable Long id) throws IncorrectIdException {
        return facilityTypeService.getFacilityTypeById(id);
    }

    @GetMapping(value = "/reminders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Reminder> reminders(@RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "ASC") String sortBy) {

        return reminderService.getReminders(pageNo, pageSize, sortBy);

    }

    @GetMapping("/reminder/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Reminder reminder(@PathVariable Long id) throws IncorrectIdException {

        return reminderService.getReminderByIdOrThrow(id);
    }
    @GetMapping(value = "/reminders-by-reservation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Reminder> remindersByReservation(@RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(defaultValue = "ASC") String sortBy,
                                                 @PathVariable Long reservationId) {
        return reminderService.findAllByReservationSpecification( reservationId);
    }



    @PostMapping(value = "/add-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Person> addUser(@RequestBody Person person) throws MobileNumberException, IncorretNameException {
        checkEmailPersonExists(person);
        personService.savePerson(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping(value = "/add-reservation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) throws FieldValueException, IncorrectIdException, PersonNotFoundException, IncorrectDateException, BlockedDaysException {

        Reservation reservation = reservationService.updateOrSaveReservation(reservationRequestDTO, null);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }


    @PostMapping(value = "/add-property")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Property> addProperty(@RequestBody Property property) throws IncorretNameException {
        checkIfEmailPropertyExists(property);
        propertyService.createProperty(property);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    @PostMapping(value = "/save-coupon")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Status saveCoupon(@RequestBody CouponRequestDTO couponRq) {
        Status status = new Status();
        String code = couponRq.getCode();
        String validFrom = couponRq.getValidFrom();
        String validTo = couponRq.getValidTo();
        double discount = couponRq.getDiscount();
        boolean isActive = couponRq.isActivCoupon();
        Coupon coupon = couponService.save(code, validFrom, validTo, discount, isActive);

        status.setMessage("Coupon added successfully !");
        status.setId(coupon.getId());

        return status;
    }
    @PostMapping(path = "/save-facility")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Facility saveFacility(@RequestBody FacilityRq facilityRq) throws IncorrectIdException {
        return facilityService.saveFacility(facilityRq);

    }

    @PostMapping(path = "/save-facility-type")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public FacilityType saveFacilityType(@RequestBody FacilityType facilityType){
        return facilityTypeService.saveFacilityType(facilityType);
    }
    @Transactional
    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Status saveReminder(@RequestParam Long reservationId) throws IncorrectIdException {

        Status status = new Status();
        Reminder reminder = reminderService.saveReminder(reservationId);

        status.setMessage("Reminder saved successfully !");
        status.setId(reminder.getId());

        return status;
    }



    @PutMapping(value = "/update-role/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> updateRole(@PathVariable Long id, @RequestBody Role roles) {
        return checkIfIdExistsAndSetRole(id, roles);
    }

    @PutMapping(value = "/update-person/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personRequest) throws IncorrectIdException, MobileNumberException, IncorretNameException {
        checkIfPersonIdExists(id);
        Person person = personService.updateOrSavePerson(personRequest, id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PutMapping(value = "/update-property/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property propertyRequest) throws IncorrectIdException {
        checkPropertyIdExists(id);
        Property property = propertyService.updateOrSaveProperty(propertyRequest, id);
        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    @PutMapping(value = "/update-reservation/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationRequestDTO reservationRequestDTO, @PathVariable Long id) throws FieldValueException, IncorrectIdException, PersonNotFoundException, IncorrectDateException, BlockedDaysException {

        Reservation reservation = reservationService.updateOrSaveReservation(reservationRequestDTO, id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PutMapping(value = "/update/coupon/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Status updateCoupon(@PathVariable Long id, @RequestBody CouponRequestDTO couponRq) throws Exception {
        Status status = new Status();
        String code = couponRq.getCode();
        String validFrom = couponRq.getValidFrom();
        String validTo = couponRq.getValidTo();
        double discount = couponRq.getDiscount();
        boolean isActive = couponRq.isActivCoupon();
        Coupon coupon = couponService.update(id, code, validFrom, validTo, discount, isActive);

        status.setMessage("Coupon updated successfully !");
        status.setId(coupon.getId());

        return status;
    }
    @PutMapping(path = "/update/facility/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Facility updateFacility(@PathVariable Long id, @RequestBody FacilityRq facilityRq) throws IncorrectIdException {
        return facilityService.updateFacility(id, facilityRq);

    }

    @PutMapping(path = "/update/facility-type/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public FacilityType updateFacilityType(@PathVariable Long id, @RequestBody FacilityType facilityType) throws IncorrectIdException {
        return facilityTypeService.updateFacilityType(id,facilityType);
    }

    @Transactional
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Status updateReminder(@RequestBody Reminder reminderRq, @PathVariable Long id) throws IncorrectIdException {

        Status status = new Status();
        Reminder reminder = reminderService.updateReminder(id, reminderRq);

        status.setMessage("Reminder updated successfully !");
        status.setId(reminder.getId());

        return status;
    }


    @DeleteMapping(value = "/remove-person/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> removePerson(@PathVariable Long id) throws IncorrectIdException {
        checkIfPersonIdExists(id);
        personService.deletePerson(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


    @DeleteMapping(value = "/remove-property/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> removeProperty(@PathVariable Long id) throws IncorrectIdException {
        checkPropertyIdExists(id);
        personService.deletePerson(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping(value = "/remove-reservation/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> deleteReservation(@PathVariable Long id) throws IncorrectIdException {
        reservationVerification(id);
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(id, HttpStatus.OK);

    }

    @DeleteMapping(value = "/coupon/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Status deleteCoupon(@PathVariable Long id) {
        Status status = new Status();
        Long couponId = couponService.deleteCoupon(id);

        status.setMessage("Coupon deleted successfully !");
        status.setId(couponId);

        return status;
    }
    @DeleteMapping(path = "/delete/facility/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long deleteFacility(@PathVariable Long id) throws IncorrectIdException {
        return facilityService.deleteFacility(id);
    }
    @DeleteMapping(path = "/delete/facility-type/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long deleteFacilityType(@PathVariable Long id) throws IncorrectIdException {
        return facilityTypeService.deleteFacilityType(id);
    }
    @Transactional
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Status deleteReminder(@PathVariable Long id) throws IncorrectIdException {
        Status status = new Status();

        reminderService.deleteReminder(id);

        status.setMessage("Reminder deleted successfully !");
        status.setId(id);
        return status;
    }

    private void checkPropertyIdExists(Long id) throws IncorrectIdException {
        Optional<Property> findById = propertyService.findById(id);
        if (findById.isEmpty()) {
            throw new IncorrectIdException("This id " + id + " doesn't exists!");
        }
    }

    private void checkEmailPersonExists(Person person) throws IncorretNameException {
        Optional<Person> checkEmail = personService.findByEmail(person.getEmail());
        if (checkEmail.isPresent()) {
            throw new IncorretNameException("This email: " + person.getEmail() + " already exists!");
        }
    }

    private void checkIfEmailPropertyExists(Property property) throws IncorretNameException {
        Optional<Property> checkEmail = propertyService.findByPropertyEmail(property.getPropertyEmail());
        if (checkEmail.isPresent()) {
            throw new IncorretNameException("This email: " + property.getPropertyEmail() + " already exists!");
        }
    }

    private ResponseEntity<Long> checkIfIdExistsAndSetRole(Long id, Role roles) {
        Optional<Person> optionalPerson = personService.findById(id);
        if (optionalPerson.isPresent()) {
            optionalPerson.ifPresent(user -> {
                user.setRole(roles);
                try {
                    personService.savePerson(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
    }

    private void checkIfPersonIdExists(Long id) throws IncorrectIdException {
        Optional<Person> findById = personService.findById(id);
        if (findById.isEmpty()) {
            throw new IncorrectIdException("This id doesn't exists!");
        }

    }

    private void reservationVerification(Long id) throws IncorrectIdException {
        if (id != null) {
            Optional<Reservation> reservationById = reservationService.getReservationById(id);

            if (reservationById.isEmpty()) {
                throw new NotFoundException("Reservation was not found!");
            }
        }
    }
}
