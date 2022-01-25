package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.example.util.AppProperties;

@Configuration
@EnableScheduling
public class CronJobService {
    private static Logger logger = LoggerFactory.getLogger(CronJobService.class);

    @Autowired
    private ThemeService themeService;
    @Autowired
    private WtsMessageService wtsMessageService;
    @Autowired
    private AppProperties appProperties;

    // temporarily turn off (by Mr.Khoi)
    // @Scheduled(cron = "${schedule.import.theme}")
    public void importThemes() {
        if (appProperties.isDisableSchedule()) return;

        try {
            logger.info("-----------------------------start import themes-------------------------");
            themeService.importThemeFromCore();
            logger.info("-----------------------------end import themes-------------------------");
        } catch (Exception e) {
            logger.info("Fail import themes");
            logger.error(e.getMessage(), e);
        }
    }

    @Scheduled(cron = "${schedule.refesh.wts.list.message}")
    public void refreshListWtsMessage() {
        if (appProperties.isDisableSchedule()) return;

        try {
            logger.info("-----------------------------start refresh wts list message -------------------------");
            wtsMessageService.refreshListErrorMessage();
            logger.info("-----------------------------end refresh wts list message -------------------------");
        } catch (Exception e) {
            logger.info("Fail to refresh wts list message");
            logger.error(e.getMessage(), e);
        }
    }
}
