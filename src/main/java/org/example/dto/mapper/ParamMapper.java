package org.example.dto.mapper;

import org.example.dto.response.ParameterRSDTO;
import org.example.entity.Parameter;
import org.mapstruct.Mapper;

@Mapper
public interface ParamMapper {
    ParameterRSDTO entityToDTO(Parameter parameter);
}