package com.example.springwebfluxproject.controller;

import com.example.springwebfluxproject.model.Template;
import com.example.springwebfluxproject.model.TemplateDto;
import com.example.springwebfluxproject.model.TemplateMapper;
import com.example.springwebfluxproject.service.TemplateReactiveService;
import com.example.springwebfluxproject.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/template")
public class TemplateReactiveController {

    private final TemplateReactiveService templateService;
    private final TemplateMapper templateMapper;

    public TemplateReactiveController(TemplateReactiveService templateService, TemplateMapper templateMapper) {
        this.templateService = templateService;
        this.templateMapper = templateMapper;
    }

    @PostMapping
    public ResponseEntity<Mono<TemplateDto>> createTemplate(@RequestBody TemplateDto dto) {
        Template template = templateMapper.dtoToTemplate(dto);
        Mono<Template> templateMono = templateService.saveTemplate(template);
        return ResponseEntity.ok(templateMono.map(templateMapper::templateToDto));
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<Mono<TemplateDto>> getTemplate(@PathVariable Integer templateId) {
        Mono<Template> templateMono = templateService.getTemplate(templateId);
        return ResponseEntity.ok(templateMono.map(templateMapper::templateToDto));
    }

    @GetMapping("/customQuery")
    public ResponseEntity<Mono<TemplateDto>> customQuery() {
        Mono<Template> templateMono = templateService.customQuery();
        Mono<TemplateDto> templateDtoMono = templateMono.map(templateMapper::templateToDto);
        return ResponseEntity.ok(Mono.just(new TemplateDto()));
    }

    @GetMapping("/backpressure")
    public Mono<String> backPressure() {
        templateService.backPressure();
        return Mono.just("");
    }

    @GetMapping("/wrapBlockingCode")
    public void wrapBlockingCode() {
        templateService.wrapBlockingCode();
    }
}
