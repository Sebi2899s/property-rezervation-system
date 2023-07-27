package ro.itschool.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.itschool.Booking.Dto.CouponRequestDTO;
import ro.itschool.Booking.entity.Coupon;
import ro.itschool.Booking.service.CouponService;

@RestController
@RequestMapping("/api")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping(value = "/coupon/{id}")
    public Coupon getCouponById(@RequestParam Long id) throws Exception {
        return couponService.getCoupon(id);
    }

    @PostMapping(value = "/coupon")
    public Coupon saveCoupon(@RequestBody CouponRequestDTO couponRq) {
        String code = couponRq.getCode();
        String validFrom = couponRq.getValidFrom();
        String validTo = couponRq.getValidTo();
        double discount = couponRq.getDiscount();
        boolean isActive = couponRq.isActivCoupon();
        return couponService.save(code,validFrom,validTo,discount,isActive);
    }

    @PutMapping(value = "/update/{id}")
    public Coupon updateCoupon(@PathVariable Long id,@RequestBody CouponRequestDTO couponRq) throws Exception {
        String code = couponRq.getCode();
        String validFrom = couponRq.getValidFrom();
        String validTo = couponRq.getValidTo();
        double discount = couponRq.getDiscount();
        boolean isActive = couponRq.isActivCoupon();
        return couponService.update(id,code,validFrom,validTo,discount,isActive);
    }

    @DeleteMapping(value = "/coupon/{id}")
    public Long deleteCoupon(@RequestParam Long id) {
        return couponService.deleteCoupon(id);
    }
}
