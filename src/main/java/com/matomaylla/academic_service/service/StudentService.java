package com.matomaylla.academic_service.service;

import com.matomaylla.academic_service.dto.StudentDTO;
import com.matomaylla.academic_service.entity.Student;
import com.matomaylla.academic_service.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        log.info("Obteniendo todos los estudiantes con carga optimizada");
        return studentRepository.findAllWithEnrollmentsAndCourses().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Page<StudentDTO> getAllStudentsPaged(int page, int size) {
        log.info("Obteniendo estudiantes paginados (página: {}, tamaño: {})", page, size);
        Page<Student> studentPage = studentRepository.findAllWithEnrollmentsAndCoursesPaged(PageRequest.of(page, size));
        List<StudentDTO> studentDTOs = studentPage.getContent().stream()
                .map(this::convertToDTO)
                .toList();
        return new PageImpl<>(studentDTOs, studentPage.getPageable(), studentPage.getTotalElements());
    }

    private StudentDTO convertToDTO(Student student) {
        List<String> enrolledCourses = student.getEnrollments().stream()
                .map(enrollment -> enrollment.getCourse().getTitle())
                .toList();
                
        return new StudentDTO(
            student.getId(),
            student.getName(),
            enrolledCourses
        );
    }
}