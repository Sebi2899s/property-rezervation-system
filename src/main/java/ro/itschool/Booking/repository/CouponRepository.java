package ro.itschool.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ro.itschool.Booking.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CrudRepository<Coupon,Long> {
}
