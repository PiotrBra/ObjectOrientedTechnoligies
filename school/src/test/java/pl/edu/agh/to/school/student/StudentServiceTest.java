package pl.edu.agh.to.school.student;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.grade.Grade;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // Automatycznie cofa operacje po każdym teście
@AutoConfigureTestDatabase
public class StudentServiceTest {

    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        // Tworzymy przykładowego studenta do testów
        testStudent = new Student("Jan", "Kowalski", LocalDate.of(2000, 1, 1), "12345");
        studentService.saveStudent(testStudent);
    }

    @Test
    void shouldCalculateAverageGradeForStudent() {
        // Dodajemy oceny do studenta
        Grade grade1 = new Grade(4, null);
        Grade grade2 = new Grade(5, null);

        testStudent.giveGrade(grade1);
        testStudent.giveGrade(grade2);
        studentService.saveStudent(testStudent);

        // Wyliczamy średnią ocen
        Double average = studentService.getStudentAverageGrade(testStudent.getId());

        // Sprawdzamy poprawność średniej
        assertThat(average).isEqualTo(4.5);
    }

    @Test
    void shouldReturnNullWhenNoGrades() {
        // Wyliczamy średnią ocen dla studenta bez ocen
        Double average = studentService.getStudentAverageGrade(testStudent.getId());

        // Sprawdzamy, czy zwracana wartość to null
        assertThat(average).isNull();
    }

    @Test
    void shouldThrowExceptionForNonExistingStudent() {
        // Próba wyliczenia średniej dla nieistniejącego studenta
        Integer nonExistingId = 999;

        // Sprawdzamy, czy rzucany jest wyjątek
        try {
            studentService.getStudentAverageGrade(testStudent.getId());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Student not found with id: " + nonExistingId);
        }
    }

    @Test
    void shouldAddNewGradeToStudent() {
        // Dodajemy nową ocenę
        Grade grade = new Grade(5, null);
        studentService.getStudentAverageGrade(testStudent.getId());

        // Pobieramy studenta z repozytorium i sprawdzamy jego oceny
        Optional<Student> updatedStudent = studentService.getStudentById(testStudent.getId());
        Assertions.assertThat(updatedStudent).isPresent();
        assertThat(updatedStudent.get().getGrades()).hasSize(1);
        assertThat(updatedStudent.get().getGrades().get(0).getGradeValue()).isEqualTo(5);
    }
}
