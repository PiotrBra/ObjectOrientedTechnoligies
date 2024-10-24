package pl.edu.agh.iisg.to.service;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.repository.StudentRepository;
import pl.edu.agh.iisg.to.session.TransactionService;

import javax.persistence.EntityNotFoundException;
import java.util.*;

public class SchoolService {

    private final TransactionService transactionService;

    private final StudentDao studentDao;

    private final CourseDao courseDao;

    private final GradeDao gradeDao;

    private final StudentRepository studentRepository;

    public SchoolService(TransactionService transactionService, StudentDao studentDao, CourseDao courseDao, GradeDao gradeDao, StudentRepository studentRepository) {
        this.transactionService = transactionService;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.gradeDao = gradeDao;
        this.studentRepository = studentRepository;
    }

    public boolean enrollStudent(final Course course, final Student student) {
        // TODO - implement
        return transactionService.doAsTransaction( () -> {
            if (studentRepository.findAllByCourseName(course.name()).contains(student)){
                return false;
            }
            course.studentSet().add(student);
            student.courseSet().add(course);
            return true;
        }).orElse(false);
    }

    public boolean removeStudent(int indexNumber) {
        return transactionService.doAsTransaction(() ->{
            Student student = studentRepository.getByIndexNumber(indexNumber).
                    orElseThrow(() -> new EntityNotFoundException("student not found"));

            studentRepository.remove(student);

            return true;
        }).orElse(false);
    }

    public boolean gradeStudent(final Student student, final Course course, final float gradeValue) {
        // TODO - implement
        return transactionService.doAsTransaction(() -> {
            Grade grade = new Grade(student, course, gradeValue);

            gradeDao.save(grade);

            student.gradeSet().add(grade);
            course.gradeSet().add(grade);

            return true;
        }).orElse(false);
    }

    public Map<String, List<Float>> getStudentGrades(String courseName) {
        return transactionService.doAsTransaction(() -> {
            Course course = courseDao.findByName(courseName)
                    .orElseThrow(() -> new EntityNotFoundException("course not found"));
            Map<String, List<Float>> report = new HashMap<>();

            for (Grade grade : course.gradeSet()) {
                String key = grade.student().firstName() + " " + grade.student().lastName();
                report.computeIfAbsent(key, k -> new ArrayList<>()).add(grade.grade());
            }

            // Sort the grades for each student
            for (Map.Entry<String, List<Float>> entry : report.entrySet()) {
                Collections.sort(entry.getValue());
            }

            return report;
        }).orElse(Collections.emptyMap());
    }
}
