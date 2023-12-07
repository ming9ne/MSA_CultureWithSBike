package com.sth.couponservice.repository;

import com.sth.couponservice.model.entity.Coupon;
import com.sth.couponservice.model.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByCouponAndUserID(Coupon coupon, String userId);
    List<UserCoupon> findAllByUserID(String userId);
}
