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

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer_register")
@Where(clause = "deleted_at IS NULL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomerRegister extends BaseEntity {

    private static Logger logger = LoggerFactory.getLogger(CustomerRegister.class);

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

    @Column(name = "user_name", columnDefinition = "varchar(50)")
    private String userName;

    @Column(name = "step", columnDefinition = "int")
    private Integer step;

    @Column(name = "ref_code", columnDefinition = "varchar(20)")
    private String refCode;

    @Column(name = "cif", columnDefinition = "varchar(50)")
    private String cif;
}
