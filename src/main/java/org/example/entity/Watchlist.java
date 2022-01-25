package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "watchlist")
@Where(clause = "deleted_at IS NULL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Watchlist extends BaseEntity {
    @Column(name = "customer_id", nullable = false, columnDefinition = "bigint")
    private Long customerId;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    // not use right now, use is_share_portfolio in customer_Setting
    @Column(name = "share", columnDefinition = "bit(1)")
    private Boolean share;
}
