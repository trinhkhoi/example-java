package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.CustomerTopic;
import org.example.repository.CustomerTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.common.exception.BusinessException;
import org.common.utils.DataUtil;
import org.example.dto.mapper.CustomerTopicMapper;
import org.example.dto.request.TopicRequest;
import org.example.dto.response.CustomerTopicDTO;
import org.example.entity.Customer;
import org.example.service.CustomerService;
import org.example.service.CustomerTopicService;
import org.example.service.TopicService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.util.Constant.SPLIT_COMMA;

@Slf4j
@Service
public class CustomerTopicServiceImpl extends BaseServiceImpl implements CustomerTopicService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CustomerTopicRepository customerTopicRepository;
    @Autowired
    private CustomerTopicMapper mapper;

    @Override
    public void selectedTopics(TopicRequest request) throws BusinessException {
        Customer customer = customerService.getCurrentCustomer();
        if (!DataUtil.isNullOrEmpty(request.getTopicCodes())) {
            List<String> topicCodes = Arrays.asList(request.getTopicCodes().split(SPLIT_COMMA));
            if (!topicCodes.isEmpty()) {
                List<String> existedThemeCodes = customerTopicRepository.findListCustomerTopics(customer.getId(), topicCodes);
                for (String topicCode : topicCodes) {
                    if (!existedThemeCodes.contains(topicCode)) {
                        customerTopicRepository.save(CustomerTopic.builder()
                                .idCustomer(customer.getUserId())
                                .topicCode(topicCode)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build());
                        // increase number of customer selected for topic
                        topicService.increaseCustomerSelected(topicCode);
                    }
                }
            }
        }
    }

    @Override
    public List<CustomerTopicDTO> findAllTopicByCustomerId() throws BusinessException {
        Long customerId = getAuthentication().getUserId();
        List<CustomerTopic> customerTopics = customerTopicRepository.findAllByIdCustomer(customerId);
        if (customerTopics.isEmpty()) {
            return new ArrayList<>();
        } else {
            return customerTopicRepository.findAllByIdCustomer(customerId).stream().
                    map(mapper::entityToDTO).collect(Collectors.toList());
        }
    }

    @Override
    public void unselectedTopics(TopicRequest request) throws BusinessException {
        Long customerId = getAuthentication().getUserId();
        List<String> topicCodes = Arrays.asList(request.getTopicCodes().split(SPLIT_COMMA));
        List<String> customerTopicCodes = customerTopicRepository.findListCustomerTopics(customerId, topicCodes);
        customerTopicRepository.deleteByIdCustomerAndTopicCode(customerId, customerTopicCodes);
        // decrease number of cutomer selected for topic
        for (String topicCode : customerTopicCodes) {
            topicService.decreaseCustomerSelected(topicCode);
        }
    }
}
