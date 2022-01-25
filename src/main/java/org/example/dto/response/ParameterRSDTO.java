package org.example.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParameterRSDTO {
    private String paramCode;
    private String title;
    private String description;
}