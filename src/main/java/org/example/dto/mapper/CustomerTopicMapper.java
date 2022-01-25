package org.example.dto.mapper;

import org.example.dto.response.CustomerTopicDTO;
import org.example.entity.CustomerTopic;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerTopicMapper {
    CustomerTopicDTO entityToDTO(CustomerTopic customerTopic);
}
