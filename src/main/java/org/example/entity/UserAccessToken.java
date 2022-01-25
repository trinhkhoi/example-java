package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_access_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserAccessToken extends BaseEntity {
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(name = "is_admin", nullable = false, columnDefinition = "bit(1)")
    private Boolean isAdmin;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "authorities", nullable = false)
    private String authorities;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "expired_at", columnDefinition = "datetime")
    private LocalDateTime expiredAt;
}
