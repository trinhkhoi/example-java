package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.common.security.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity implements UserDetails {

    @Column(name = "authorities", nullable = false)
    private String authorities;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Override
    public Long getUserId() {
        return getId();
    }

    @Override
    @JsonIgnore
    public boolean isAdmin() {
        return true;
    }
}
