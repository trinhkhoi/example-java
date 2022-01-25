package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_register_temp")
@Where(clause = "deleted_at IS NULL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomerRegisterTemp extends BaseEntity {

    private static Logger logger = LoggerFactory.getLogger(CustomerRegisterTemp.class);

    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;

    @Column(name = "display_name", columnDefinition = "varchar(255)")
    private String displayName;

    @Column(name = "avatar", columnDefinition = "varchar(255)")
    private String avatar;

    @Column(name = "phone", columnDefinition = "varchar(20)")
    private String phone;

    @Column(name = "email", columnDefinition = "varchar(50)")
    private String email;

    @Column(name = "user_name", columnDefinition = "varchar(255)")
    private String userName;

    @Column(name = "identity_no", columnDefinition = "varchar(255)")
    private String identityNo ;

    @Column(name = "issue_date", columnDefinition = "date")
    private LocalDate issueDate;

    @Column(name = "issue_by", columnDefinition = "varchar(255)")
    private String issueBy;

    @Column(name = "city", columnDefinition = "varchar(255)")
    private String city;

    @Column(name = "address", columnDefinition = "varchar(255)")
    private String address;

    @Column(name = "dob", columnDefinition = "date")
    private LocalDate dob;

    @Column(name = "gender", columnDefinition = "bit(1)")
    private Boolean gender;

    @Column(name = "is_accept_condition", columnDefinition = "bit(1)")
    private Boolean isAcceptCondition;

    @Column(name = "otp", columnDefinition = "varchar(50)")
    private String otp;

    @Column(name = "number_sent", columnDefinition = "int")
    private Integer numberSent;

    @Column(name = "is_verify_email", columnDefinition = "bit(1)")
    private Boolean isVerifyEmail;
}
