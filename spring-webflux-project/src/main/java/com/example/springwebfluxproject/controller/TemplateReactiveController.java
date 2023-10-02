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

    @GetMapping
    public void customQuery() {
        templateService.customQuery();
    }
}
