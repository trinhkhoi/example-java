package org.example.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.repository.CustomerDefaultAvatarRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class CustomerUtil {
    private final CustomerDefaultAvatarRepository customerDefaultAvatarRepository;

    public String setDefaultAvatar(String customerName) {
        String firstLetter = StringUtils.isBlank(customerName) ? "none" : customerName.substring(0, 1);
        List<String> listAvatar = customerDefaultAvatarRepository.findAllAvatarUrlByLetter(firstLetter);
        Random rand = new Random();
        String randomAvatarUrl = "P";
        if (!listAvatar.isEmpty()) {
            randomAvatarUrl = listAvatar.get(rand.nextInt(listAvatar.size()));
        }
        return randomAvatarUrl;
    }

}
