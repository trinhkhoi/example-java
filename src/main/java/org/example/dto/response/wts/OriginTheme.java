package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.common.utils.DataUtil;
import org.example.dto.response.ThemeStockDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginTheme {
    private int reqCount;
    private String thsCode;
    private String thsName;
    private String thsType;
    private String thsUrlPic;
    private String rmrk;
    private int wrktmp;
    private List<ThemeSymbol> themdtl;

    public List<ThemeStockDto> convertThemeSymbolToResponse() {
        return themdtl.stream().map(symbol -> ThemeStockDto.builder()
                .stock_code(symbol.getSymbol())
                .stock_name(symbol.getSymbolNm())
                .last_price(DataUtil.safeToDouble(symbol.getCurPrice()))
                .build()).collect(Collectors.toList());
    }

    public String getThsUrlPic() {
        return StringUtils.isBlank(this.thsUrlPic) ? StringUtils.EMPTY : this.thsUrlPic.trim();
    }
}
