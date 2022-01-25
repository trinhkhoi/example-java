package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "topic")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Topic extends BaseEntity {

    private static Logger logger = LoggerFactory.getLogger(Topic.class);

    @Column(name = "topic_code", columnDefinition = "varchar(50)")
    private String topicCode;

    @Column(name = "topic_name", columnDefinition = "varchar(255)")
    private String topicName;

    @Column(name = "total_selected", columnDefinition = "int")
    private Integer totalSelected;
}
