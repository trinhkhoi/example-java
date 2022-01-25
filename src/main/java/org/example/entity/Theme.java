package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.response.CustomerLatestDto;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.common.utils.JSON;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: khoitd
 * Date: 2021-05-13 13:42
 * Description: this file defines the models of theme.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "theme")
@Where(clause = "deleted_at IS NULL")
public class Theme extends BaseEntity{

    private static Logger logger = LoggerFactory.getLogger(Theme.class);

    @Column(name = "theme_code", columnDefinition = "varchar(255)")
    private String themeCode;

    @Column(name = "theme_name", columnDefinition = "varchar(255)")
    private String themeName;

    @Column(name = "theme_type", columnDefinition = "varchar(255)")
    private String themeType;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "wrktmp", columnDefinition = "int")
    private Integer wrktmp;

    @Column(name = "url", columnDefinition = "text")
    private String url;

    @Column(name = "bg_image", columnDefinition = "text")
    private String bgImage;

    @Column(name = "total_subsribe", columnDefinition = "int")
    private Integer totalSubsribe;

    @JsonIgnore
    @Column(name = "lastest_subscribe", columnDefinition = "text")
    private String lastestSubscribe;

    public List<CustomerLatestDto> getListLastestSubscribe() {
        if (StringUtils.isBlank(lastestSubscribe)) {
            return new ArrayList<>();
        }
        try {
            List<CustomerLatestDto> latestSubscribes = JSON.fromJson(lastestSubscribe, List.class);
            return latestSubscribes;
        } catch(Exception ex) {
            logger.error("Error during convert String lastestSubscribe to list: " + lastestSubscribe);
        }
        return new ArrayList<>();
    }
}
