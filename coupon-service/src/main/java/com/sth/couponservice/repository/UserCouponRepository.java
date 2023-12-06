package com.sth.couponservice.repository;

import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.model.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByCouponAndUserID(Coupon coupon, String userId);
}
