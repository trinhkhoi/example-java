package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_default_avatar")
@Getter
@Setter
public class CustomerDefaultAvatar{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "first_letter", nullable = false, columnDefinition = "varchar(10)")
    private String firstLetter;

    @Column(name = "avatar_url", nullable = false, columnDefinition = "varchar(255)")
    private String avatarUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "created_at", columnDefinition = "datetime")
    private LocalDateTime createdAt;



}
