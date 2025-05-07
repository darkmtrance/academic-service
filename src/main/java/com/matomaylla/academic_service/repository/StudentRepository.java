package com.matomaylla.academic_service.repository;

import com.matomaylla.academic_service.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    @Query("SELECT DISTINCT s FROM Student s " +
           "LEFT JOIN FETCH s.enrollments e " +
           "LEFT JOIN FETCH e.course")
    List<Student> findAllWithEnrollmentsAndCourses();
    
    @Query(value = "SELECT DISTINCT s FROM Student s " +
           "LEFT JOIN FETCH s.enrollments e " +
           "LEFT JOIN FETCH e.course",
           countQuery = "SELECT COUNT(DISTINCT s) FROM Student s")
    Page<Student> findAllWithEnrollmentsAndCoursesPaged(Pageable pageable);
}
