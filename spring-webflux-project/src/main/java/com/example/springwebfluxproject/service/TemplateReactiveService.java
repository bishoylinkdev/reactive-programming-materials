package com.example.springwebfluxproject.service;

import com.example.springwebfluxproject.model.Template;
import com.example.springwebfluxproject.model.TemplateMapper;
import com.example.springwebfluxproject.model.TemplateRecord;
import com.example.springwebfluxproject.repository.TemplateReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class TemplateReactiveService {
    private final TemplateReactiveRepository templateReactiveRepository;
    private final TemplateMapper templateMapper;

    public TemplateReactiveService(TemplateReactiveRepository templateReactiveRepository, TemplateMapper templateMapper) {
        this.templateReactiveRepository = templateReactiveRepository;
        this.templateMapper = templateMapper;
    }

    public Mono<Template> saveTemplate(Template template) {
        TemplateRecord record = templateMapper.templateToRecord(template);

        Mono<TemplateRecord> recordMono = templateReactiveRepository.save(record);
//        return recordMono.flatMap(savedRecord -> Mono.just(templateMapper.recordToTemplate(savedRecord)));
        return recordMono.map(templateMapper::recordToTemplate);
    }

    public void customQuery() {
//        Mono<String> stringMono = templateReactiveRepository.customQuery(10);
//        stringMono.subscribe();
//        stringMono.doOnSuccess(s -> log.info("custom query : {}", s));
        Mono<String> mono1 = Mono.fromCallable(() -> {
            Thread.sleep(10000);
            log.info("after thread sleep");
            return "mono_value";
        });
        mono1.doOnSuccess(s -> log.info("consumed value : {}", s))
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
        log.info("method finish");
    }
}
