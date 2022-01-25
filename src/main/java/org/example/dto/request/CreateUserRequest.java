package org.example.dto.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String authorities;

    public boolean isValid() {
        return StringUtils.isNotBlank(username)
                && StringUtils.isNotBlank(password)
                && StringUtils.isNotBlank(authorities);
    }

    public String getUsername() {
        return username != null ? username.replaceAll("\\s+", "") : username;
    }

    public String getPassword() {
        return password != null ? password.replaceAll("\\s+", "") : password;
    }

    public String getAuthorities() {
        return authorities != null ? authorities.replaceAll("\\s+", "") : authorities;
    }
}
