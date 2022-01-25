package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.entity.Parameter;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    Parameter findByParamCode(String paramCode);

    List<Parameter> findAllByParamCodeIn(List<String> paramCode);
}
