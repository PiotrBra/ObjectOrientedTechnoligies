package pl.edu.agh.to.school.student;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.school.grade.Grade;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentByIndexNumber(String indexNumber){
        return studentRepository.findByIndexNumber(indexNumber);
    }

    public Optional<Student> getStudentById(int id){
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Double getStudentAverageGrade(int studentId) {
        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isEmpty()) {
            throw new IllegalArgumentException("Student o podanym ID nie istnieje.");
        }

        return student.get().calculateAverageGrade();
    }
}
