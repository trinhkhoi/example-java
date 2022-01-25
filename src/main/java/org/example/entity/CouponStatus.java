package org.example.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "coupon_status")
@Entity
@Data
public class CouponStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "coupon_code", columnDefinition = "varchar(100)")
    private String couponCode;

    @Column(name = "status", columnDefinition = "varchar(50)")
    private String status;

    @Column(name = "button", columnDefinition = "varchar(100)")
    private String button;

    @Column(name = "status_title", columnDefinition = "varchar(100)")
    private String statusTitle;

    @Column(name = "status_description", columnDefinition = "varchar(100)")
    private String statusDescription;
}
