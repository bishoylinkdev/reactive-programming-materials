package com.example.springwebfluxproject.controller;

import com.example.springwebfluxproject.model.Template;
import com.example.springwebfluxproject.model.TemplateDto;
import com.example.springwebfluxproject.model.TemplateMapper;
import com.example.springwebfluxproject.service.TemplateService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/template")
@ConditionalOnProperty(value = "spring.r2dbc.enabled", havingValue = "false")
public class TemplateController {

    private final TemplateService templateService;
    private final TemplateMapper templateMapper;

    public TemplateController(TemplateService templateService, TemplateMapper templateMapper) {
        this.templateService = templateService;
        this.templateMapper = templateMapper;
    }

    @PostMapping
    public ResponseEntity<TemplateDto> createTemplate(@RequestBody TemplateDto dto) {
        Template template = templateMapper.dtoToTemplate(dto);
        Template savedTemplate = templateService.saveTemplate(template);
        return ResponseEntity.ok(templateMapper.templateToDto(savedTemplate));
    }
}
