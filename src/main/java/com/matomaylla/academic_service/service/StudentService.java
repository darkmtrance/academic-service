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
        log.info("Iniciando carga de todos los estudiantes con matrículas (usando antipatrón N+1)");
        
        // Primer consulta: obtener todos los estudiantes
        log.debug("Ejecutando primera consulta: Obtener todos los estudiantes");
        List<Student> students = studentRepository.findAll();
        log.debug("Encontrados {} estudiantes", students.size());
        
        int enrollmentQueryCount = 0;
        int courseQueryCount = 0;
        
        // N consultas adicionales: forzar la carga de enrollments para cada estudiante
        for (Student student : students) {
            log.debug("Cargando matrículas para estudiante ID: {}, Nombre: {}", student.getId(), student.getName());
            
            // Esto forzará una consulta por cada estudiante (problema N+1)
            if (student.getEnrollments() == null) {
                student.setEnrollments(new ArrayList<>());
            }
            
            Hibernate.initialize(student.getEnrollments());
            enrollmentQueryCount++;
            
            log.debug("Encontradas {} matrículas para estudiante {}", student.getEnrollments().size(), student.getId());
            
            // También inicializar cada curso dentro de los enrollments (problema N+M)
            for (Enrollment enrollment : student.getEnrollments()) {
                log.debug("Cargando curso para matrícula ID: {}", enrollment.getId());
                Hibernate.initialize(enrollment.getCourse());
                courseQueryCount++;
            }
        }
        
        log.info("Completada la carga de todos los datos con enfoque antipatrón:");
        log.info("- 1 consulta para obtener todos los estudiantes");
        log.info("- {} consultas para obtener matrículas (problema N+1)", enrollmentQueryCount);
        log.info("- {} consultas para obtener cursos (problema N+M)", courseQueryCount);
        log.info("- Total de consultas ejecutadas: {}", 1 + enrollmentQueryCount + courseQueryCount);
        
        return students;
    }
}