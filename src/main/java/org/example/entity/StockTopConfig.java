package org.example.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stock_top_config")
@Getter
@Setter
public class StockTopConfig extends BaseEntity {
    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "image", columnDefinition = "varchar(255)")
    private String image;

    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;

    @Column(name = "unit", columnDefinition = "varchar(255)")
    private String unit;

    @Column(name = "end_point", columnDefinition = "varchar(255)")
    private String endpoint;
}
