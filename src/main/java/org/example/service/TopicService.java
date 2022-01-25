package org.example.service;

import org.example.dto.response.TopicDto;
import org.common.exception.BusinessException;

import java.util.List;

public interface TopicService {
    List<TopicDto> getListTopics() throws BusinessException;

    void increaseCustomerSelected(String topicCode) throws BusinessException;

    void decreaseCustomerSelected(String topicCode) throws BusinessException;
}
