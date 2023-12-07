package com.sth.couponservice.repository;

import com.sth.couponservice.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByCouponCode(String couponCode);

    Optional<Coupon> findByCouponCode(String couponCode);
}
