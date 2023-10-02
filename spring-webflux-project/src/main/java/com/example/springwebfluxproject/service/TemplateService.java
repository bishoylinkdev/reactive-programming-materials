package com.example.springwebfluxproject.service;

import com.example.springwebfluxproject.model.Template;
import com.example.springwebfluxproject.model.TemplateMapper;
import com.example.springwebfluxproject.model.TemplateRecord;
import com.example.springwebfluxproject.repository.TemplateRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "spring.r2dbc.enabled", havingValue = "false")
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final TemplateMapper templateMapper;

    public TemplateService(TemplateRepository templateRepository, TemplateMapper templateMapper) {
        this.templateRepository = templateRepository;
        this.templateMapper = templateMapper;
    }

    public Template saveTemplate(Template template) {
        TemplateRecord record = templateMapper.templateToRecord(template);
        TemplateRecord savedRecord = templateRepository.save(record);
        return templateMapper.recordToTemplate(savedRecord);
    }
}
