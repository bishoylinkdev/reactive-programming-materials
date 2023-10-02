package com.example.springwebfluxproject.repository;

import com.example.springwebfluxproject.model.TemplateRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@ConditionalOnProperty(value = "spring.r2dbc.enabled", havingValue = "false")
public interface TemplateRepository extends CrudRepository<TemplateRecord, String> {
}
