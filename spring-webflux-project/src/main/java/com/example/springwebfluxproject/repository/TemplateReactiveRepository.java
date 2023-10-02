package com.example.springwebfluxproject.repository;

import com.example.springwebfluxproject.model.TemplateRecord;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TemplateReactiveRepository extends ReactiveCrudRepository<TemplateRecord, Integer> {

    @Query(value = "select * , pg_sleep(:seconds) from template where id = :templateId;")
    Mono<TemplateRecord> customQuery(Integer seconds, Integer templateId);

}
