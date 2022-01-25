package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.common.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
    private String natnlNm;
    private String custNm;
    private String email;
    private String cphnNo;
    private String gend;
    private String brthdt;
    private String custIdKnd;
    private String custId;
    private String custIdIssuDate;
    private String custIdIssuLoca;
    private String perdomAddr;
    private String cttAddr;
    private String needVSDFile_YN;
}
