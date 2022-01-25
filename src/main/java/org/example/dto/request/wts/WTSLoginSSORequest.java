package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WTSLoginSSORequest {
    private String connId;
    private String connSecrNo;
    private String channel;
}
