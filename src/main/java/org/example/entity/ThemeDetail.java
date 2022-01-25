package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Author: khoitd
 * Date: 2021-05-13 13:42
 * Description: this file defines the models of theme detail.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "theme_detail")
@Where(clause = "deleted_at IS NULL")
public class ThemeDetail extends BaseEntity{

    private static Logger logger = LoggerFactory.getLogger(ThemeDetail.class);

    @Column(name = "theme_code", columnDefinition = "varchar(255)")
    private String themeCode;

    @Column(name = "stock_code", columnDefinition = "varchar(10)")
    private String stockCode;

    @Column(name = "stock_name", columnDefinition = "varchar(255)")
    private String stockName;

    @Column(name = "current_price", columnDefinition = "varchar(255)")
    private String currentPrice;

    @Column(name = "ord", columnDefinition = "int")
    private Integer ord;

}
