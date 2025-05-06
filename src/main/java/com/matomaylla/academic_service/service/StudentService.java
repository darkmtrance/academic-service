package com.matomaylla.academic_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.matomaylla.academic_service.entity.Enrollment;
import com.matomaylla.academic_service.entity.Student;
import com.matomaylla.academic_service.repository.EnrollmentRepository;
import com.matomaylla.academic_service.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @PostConstruct
    public void setup() {
        // Disable the Jackson feature that causes errors with Hibernate proxies
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
    
    @Transactional
    public List<Student> getAllStudents() {
        log.info("Starting to fetch all students with enrollments (using N+1 anti-pattern)");
        
        // Primer consulta: obtener todos los estudiantes
        log.debug("Executing first query: Find all students");
        List<Student> students = studentRepository.findAll();
        log.debug("Found {} students", students.size());
        
        int enrollmentQueryCount = 0;
        int courseQueryCount = 0;
        
        // N consultas adicionales: forzar la carga de enrollments para cada estudiante
        for (Student student : students) {
            log.debug("Loading enrollments for student ID: {}, Name: {}", student.getId(), student.getName());
            
            // Esto forzará una consulta por cada estudiante (problema N+1)
            if (student.getEnrollments() == null) {
                student.setEnrollments(new ArrayList<>());
            }
            
            Hibernate.initialize(student.getEnrollments());
            enrollmentQueryCount++;
            
            log.debug("Found {} enrollments for student {}", student.getEnrollments().size(), student.getId());
            
            // También inicializar cada curso dentro de los enrollments (problema N+M)
            for (Enrollment enrollment : student.getEnrollments()) {
                log.debug("Loading course for enrollment ID: {}", enrollment.getId());
                Hibernate.initialize(enrollment.getCourse());
                courseQueryCount++;
            }
        }
        
        log.info("Completed fetching all data with anti-pattern approach:");
        log.info("- 1 query to fetch all students");
        log.info("- {} queries to fetch enrollments (N+1 problem)", enrollmentQueryCount);
        log.info("- {} queries to fetch courses (N+M problem)", courseQueryCount);
        log.info("- Total queries executed: {}", 1 + enrollmentQueryCount + courseQueryCount);
        
        return students;
    }
}