package org.example.dto.response.parent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This is parent method when want to customize and forward the result from core to client
 * @param
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MixCoreResult {
    private String nextKey;
    private Boolean continued;
    private List<?> data;
}
