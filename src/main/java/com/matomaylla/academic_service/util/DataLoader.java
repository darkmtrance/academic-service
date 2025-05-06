package com.matomaylla.academic_service.util;


import com.matomaylla.academic_service.entity.Course;
import com.matomaylla.academic_service.entity.Student;
import com.matomaylla.academic_service.entity.Enrollment;
import com.matomaylla.academic_service.repository.CourseRepository;
import com.matomaylla.academic_service.repository.StudentRepository;
import com.matomaylla.academic_service.repository.EnrollmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Profile("data-loader")
public class DataLoader {

    private final Random random = new Random();

    @Bean
    CommandLineRunner initDatabase(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        return args -> {
            // Generar cursos
            List<Course> courses = generateCourses(100); // 100 cursos
            courseRepository.saveAll(courses);

            // Generar estudiantes
            List<Student> students = generateStudents(1000); // 1000 estudiantes
            studentRepository.saveAll(students);

            // Generar matriculas (enrollments)
            List<Enrollment> enrollments = generateEnrollments(students, courses, 5000); // 5000 matriculas
            enrollmentRepository.saveAll(enrollments);

            System.out.println("Base de datos cargada con datos de prueba");
        };
    }

    private List<Course> generateCourses(int count) {
        List<Course> courses = new ArrayList<>();
        String[] subjects = {"Matemáticas", "Física", "Química", "Historia", "Literatura",
                "Biología", "Geografía", "Economía", "Programación", "Inglés"};
        String[] levels = {"Básico", "Intermedio", "Avanzado"};

        for (int i = 1; i <= count; i++) {
            Course course = new Course();
            course.setId((long) i);
            String subject = subjects[random.nextInt(subjects.length)];
            String level = levels[random.nextInt(levels.length)];
            course.setTitle(subject + " " + level + " - " + i);
            courses.add(course);
        }
        return courses;
    }

    private List<Student> generateStudents(int count) {
        List<Student> students = new ArrayList<>();
        String[] nombres = {"Ana", "Juan", "María", "Pedro", "Luis", "Carmen", "José", "Paula"};
        String[] apellidos = {"García", "Rodríguez", "López", "Martínez", "González", "Pérez"};

        for (int i = 1; i <= count; i++) {
            Student student = new Student();
            student.setId((long) i);
            String nombre = nombres[random.nextInt(nombres.length)];
            String apellido = apellidos[random.nextInt(apellidos.length)];
            student.setName(nombre + " " + apellido);
            students.add(student);
        }
        return students;
    }

    private List<Enrollment> generateEnrollments(List<Student> students, List<Course> courses, int count) {
        List<Enrollment> enrollments = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Enrollment enrollment = new Enrollment();
            enrollment.setId((long) i);

            // Asignar un estudiante aleatorio
            Student randomStudent = students.get(random.nextInt(students.size()));
            enrollment.setStudent(randomStudent);

            // Asignar un curso aleatorio
            Course randomCourse = courses.get(random.nextInt(courses.size()));
            enrollment.setCourse(randomCourse);

            enrollments.add(enrollment);
        }
        return enrollments;
    }
}
