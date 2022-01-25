package org.example.security;

import org.springframework.stereotype.Component;
import org.common.security.encoder.Base64PasswordEncoder;

@Component
public class PasswordEncoderImpl extends Base64PasswordEncoder {
}
