package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.TopicDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.common.exception.BusinessException;
import org.common.utils.MessageUtil;
import org.example.entity.Topic;
import org.example.repository.TopicRepository;
import org.example.service.TopicService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicServiceImpl extends BaseServiceImpl implements TopicService {
    private static Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private MessageUtil messageUtil;

    @Override
    public List<TopicDto> getListTopics() throws BusinessException {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream().map(topic -> {
            return TopicDto.builder()
                    .topicCode(topic.getTopicCode())
                    .topicName(topic.getTopicName())
                    .totalSelected(topic.getTotalSelected())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void increaseCustomerSelected(String topicCode) throws BusinessException {
        topicRepository.increaseTotalSelected(topicCode);
    }

    @Override
    public void decreaseCustomerSelected(String topicCode) throws BusinessException {
        topicRepository.decreaseTotalSelected(topicCode);
    }
}
