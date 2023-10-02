package com.example.springwebfluxproject.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "template")
@Getter
@Setter
public class TemplateRecord {
    @Id
    private Integer id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "TemplateRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
