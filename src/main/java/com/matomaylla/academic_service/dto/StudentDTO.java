package com.matomaylla.academic_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class StudentDTO {
    private Long id;
    private String name;
    private List<String> enrolledCourses;

    public StudentDTO(Long id, String name, List<String> enrolledCourses) {
        this.id = id;
        this.name = name;
        this.enrolledCourses = enrolledCourses;
    }
}