package org.example.service;

import org.example.dto.request.TopicRequest;
import org.example.dto.response.CustomerTopicDTO;
import org.common.exception.BusinessException;

import java.util.List;

public interface CustomerTopicService {

    void selectedTopics(TopicRequest request) throws BusinessException;

    List<CustomerTopicDTO> findAllTopicByCustomerId() throws BusinessException;

    void unselectedTopics(TopicRequest request) throws BusinessException;
}
