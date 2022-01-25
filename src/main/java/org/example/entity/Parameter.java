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
 * Description: this file defines the models of parameter.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "parameter")
@Where(clause = "deleted_at IS NULL")
public class Parameter extends BaseEntity{

    private static Logger logger = LoggerFactory.getLogger(Parameter.class);

    @Column(name = "param_code", columnDefinition = "varchar(50)")
    private String paramCode;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}
