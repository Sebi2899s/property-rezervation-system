package ro.itschool.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.customException.IncorrectIdException;
import ro.itschool.Booking.entity.Coupon;
import ro.itschool.Booking.repository.CouponRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        coupon.setUsed(isActiv);
        coupon.setCode(code);
        coupon.setDiscount(discount);
        coupon.setValidFrom(validFrom);
        coupon.setValidTo(validTo);
        return saveCoupon(coupon);
    }

    public Coupon update(Long id,String code, String validFrom_, String validTo_, double discount, boolean isActiv) throws Exception {
        Coupon coupon = getCoupon(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate validFrom = LocalDate.parse(validFrom_, formatter);
        LocalDate validTo = LocalDate.parse(validTo_, formatter);
        coupon.setCode(code);
        coupon.setValidFrom(validFrom);
        coupon.setValidTo(validTo);
        coupon.setDiscount(discount);
        coupon.setUsed(isActiv);
        return couponRepository.save(coupon);
    }

    public Coupon getCoupon(Long id) throws IncorrectIdException {
        if (id != null) {
            Optional<Coupon> getCouponById = couponRepository.findById(id);
            Coupon coupon = getCouponById.get();
            return coupon;
        } else {
            throw new IncorrectIdException("This id: " + id + "was not found!");
        }
    }
    public Coupon getCouponOrAnEmpty(Long id){
        if (id != null) {
            Optional<Coupon> getCouponById = couponRepository.findById(id);
            Coupon coupon = getCouponById.get();
            return coupon;
        } else {
            Coupon coupon =new Coupon();
            saveCoupon(coupon);
            return coupon;
        }
    }

    public Long deleteCoupon(Long id) {
        try {
            if (id != null) {
                couponRepository.deleteById(id);
                return id;
            } else {
                throw new IncorrectIdException("This id: " + id + " was not found");
            }
        } catch (IncorrectIdException e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<Coupon> getAllCoupons() {
        List<Coupon> allCoupons = couponRepository.findAll();
        return allCoupons.isEmpty() ? new ArrayList<>() : allCoupons;
    }
}
