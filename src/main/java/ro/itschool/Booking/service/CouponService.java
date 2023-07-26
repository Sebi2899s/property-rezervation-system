package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Coupon;
import ro.itschool.Booking.repository.CouponRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;


    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon save(String code, String validFrom_, String validTo_, double discount, boolean isActiv) {
        Coupon coupon = new Coupon();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate validFrom = LocalDate.parse(validFrom_, formatter);
        LocalDate validTo = LocalDate.parse(validTo_, formatter);
        coupon.setActivCoupon(isActiv);
        coupon.setCode(code);
        coupon.setDiscount(discount);
        coupon.setValidFrom(validFrom);
        coupon.setValidTo(validTo);
        return saveCoupon(coupon);
    }

    public Coupon getCoupon(Long id) {
        Optional<Coupon> getCouponById = couponRepository.findById(id);
        Coupon coupon = getCouponById.get();
        return coupon;
    }

    public Long deleteCoupon(Long id) {
        couponRepository.deleteById(id);
        return id;
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }
}
