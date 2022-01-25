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
@Table(name = "customer_friend")
@Where(clause = "deleted_at IS NULL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomerFriend extends BaseEntity {

    private static Logger logger = LoggerFactory.getLogger(CustomerFriend.class);

    @Column(name = "id_customer", columnDefinition = "bigint")
    private Long idCustomer;

    @Column(name = "id_friend", columnDefinition = "bigint")
    private Long idFriend;

    @Column(name = "source", columnDefinition = "varchar(20)")
    private String source;
}
