package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponCoreList extends ArrayList<CouponCoreData> {
}
