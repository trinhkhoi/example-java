package org.example.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Author: khoitd
 * Date: 2021-05-12 13:42
 * Description: this file defines the models of customer theme.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "customer_theme")
@Where(clause = "deleted_at IS NULL")
public class CustomerTheme extends BaseEntity {
    @Column(name = "id_customer", columnDefinition = "bigint")
    private Long idCustomer;

    @Column(name = "theme_code", columnDefinition = "varchar(255)")
    private String themeCode;
}
