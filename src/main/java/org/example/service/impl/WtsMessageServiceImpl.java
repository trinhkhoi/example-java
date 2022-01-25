package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.helper.PolaServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.common.exception.BusinessException;
import org.example.service.WtsMessageService;

@Service
@Slf4j
public class WtsMessageServiceImpl implements WtsMessageService {
    private static Logger logger = LoggerFactory.getLogger(WtsMessageServiceImpl.class);
    @Autowired
    private PolaServiceHelper polaServiceHelper;


    @Override
    public void refreshListErrorMessage() throws BusinessException {
        polaServiceHelper.getListMessage();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            refreshListErrorMessage();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
