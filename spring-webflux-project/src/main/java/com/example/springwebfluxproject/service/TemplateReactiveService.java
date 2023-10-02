package com.example.springwebfluxproject.service;

import com.example.springwebfluxproject.model.Template;
import com.example.springwebfluxproject.model.TemplateMapper;
import com.example.springwebfluxproject.model.TemplateRecord;
import com.example.springwebfluxproject.repository.TemplateReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

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
        TemplateRecord templateRecord = templateMapper.templateToRecord(template);
        Mono<TemplateRecord> recordMono = templateReactiveRepository.save(templateRecord);
        return recordMono.map(templateMapper::recordToTemplate);
    }

    public Mono<Template> getTemplate(Integer templateId) {
        return templateReactiveRepository.findById(templateId)
                .map(templateMapper::recordToTemplate);
    }

    public Mono<Template> customQuery() {
        Mono<TemplateRecord> recordMono = templateReactiveRepository.customQuery(10, 1);
        recordMono
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(templateRecord -> {
                    log.info("custom query thread : {}", Thread.currentThread());
                    log.info("custom query : {}", templateRecord);
                });
        log.info("Method Finished");
        return recordMono.map(templateMapper::recordToTemplate);
    }

    public void backPressure() {

        ConnectableFlux<TemplateRecord> flux1 = templateReactiveRepository.findAll()
                .publishOn(Schedulers.boundedElastic())
                .publish();
        Flux<TemplateRecord> flux2 = templateReactiveRepository.findAll()
                .publishOn(Schedulers.boundedElastic());

        flux1
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(baseSubscriber());
        flux1
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(templateRecord -> {
//                    log.info("2nd subscriber thread : {}", Thread.currentThread());
                    log.info("2nd subscriber record : {}", templateRecord);
                });

        flux1.connect();

        flux2
                .delayElements(Duration.ofSeconds(5))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(templateRecord -> {
//                    log.info("3nd subscriber thread : {}", Thread.currentThread());
                    log.info("3rd subscriber record : {}", templateRecord);
                });
    }

    private BaseSubscriber<TemplateRecord> baseSubscriber() {
        return new BaseSubscriber<>() {
            @Override
            public void hookOnSubscribe(Subscription subscription) {
                request(3);
            }

            @Override
            public void hookOnNext(TemplateRecord templateRecord) {
//                log.info("hookOnNext thread : {}", Thread.currentThread());
                log.info("hookOnNext record : {}", templateRecord);
                if (templateRecord.getId().equals(5)) {
                    log.info("Cancelling after having received record {}", templateRecord);
                    cancel();
                }
            }
        };
    }

    public void wrapBlockingCode() {
        Mono<TemplateRecord> templateRecordMono = Mono.fromCallable(() -> {
            Thread.sleep(10000);
            TemplateRecord templateRecord = new TemplateRecord();
            templateRecord.setName("template100");
            templateRecord.setDescription("description100");
            return templateRecord;
        });
        templateRecordMono
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(templateRecord -> {
                    log.info("hookOnNext thread : {}", Thread.currentThread());
                    log.info("TemplateRecord : {} ", templateRecord);
                });

    }
}
