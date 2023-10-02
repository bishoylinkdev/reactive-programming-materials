package com.example.springwebfluxproject.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    Template dtoToTemplate(TemplateDto dto);

    TemplateRecord templateToRecord(Template template);

    Template recordToTemplate(TemplateRecord record);

    TemplateDto templateToDto(Template template);
}
