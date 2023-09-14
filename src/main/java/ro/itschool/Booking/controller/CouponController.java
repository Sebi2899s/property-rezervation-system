package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.CouponRequestDTO;
import ro.itschool.Booking.entity.Coupon;
import ro.itschool.Booking.entity.Status;
import ro.itschool.Booking.service.CouponService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping(value = "/coupon/{id}")
    public Coupon getCouponById(@PathVariable Long id) throws Exception {
        return couponService.getCoupon(id);
    }

    @GetMapping(value = "/all")
    public List<Coupon> getAllCoupons(){
        return couponService.getAllCoupons();
    }

    @PostMapping(value = "/coupon")
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

    @PutMapping(value = "/update/{id}")
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

    @DeleteMapping(value = "/coupon/{id}")
    public Status deleteCoupon(@PathVariable Long id) {
        Status status = new Status();
        Long couponId = couponService.deleteCoupon(id);

        status.setMessage("Coupon deleted successfully !");
        status.setId(couponId);

        return status;
    }
}
