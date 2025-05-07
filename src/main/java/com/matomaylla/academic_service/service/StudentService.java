package com.matomaylla.academic_service.service;

import com.matomaylla.academic_service.entity.Student;
import com.matomaylla.academic_service.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
      
    public List<Student> getAllStudents() {
        log.info("Obteniendo todos los estudiantes con carga optimizada");
        return studentRepository.findAllWithEnrollmentsAndCourses();
    }
    
    public Page<Student> getAllStudentsPaged(int page, int size) {
        log.info("Obteniendo estudiantes paginados (página: {}, tamaño: {})", page, size);
        return studentRepository.findAllWithEnrollmentsAndCoursesPaged(PageRequest.of(page, size));
    }
}