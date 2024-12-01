package pl.edu.agh.to.school.student;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.course.CourseService;
import pl.edu.agh.to.school.grade.Grade;
import pl.edu.agh.to.school.grade.GradeService;

@RestController
@RequestMapping(path = "students")
public class StudentController {
    private final StudentService studentService;

    private final CourseService courseService;

    private final GradeService gradeService;
    public StudentController(StudentService studentService, CourseService courseService, GradeService gradeService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
    }
    @GetMapping
    public Object getStudentByIndexNumber(@RequestParam(value = "indexNumber", required = false) String indexNumber){
        if (indexNumber != null) {
            return studentService.getStudentByIndexNumber(indexNumber)
                    .orElseThrow(() -> new RuntimeException("Student not found with indexNumber: " + indexNumber));
        }
        // Jeśli nie podano numeru indeksu, zwróć listę wszystkich studentów
        return studentService.getStudents();
    }

    @PostMapping("/{studentId}/grade")
    @Transactional
    public Student giveGrade(@PathVariable int studentId, @RequestBody Grade grade) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Course course = courseService.getCourseById(grade.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + grade.getCourse().getId()));
        Grade grade1 = new Grade();
        grade1.setGradeValue(grade1.getGradeValue());
        grade1.setCourse(course);
        gradeService.saveGrade(grade1);
        student.giveGrade(grade1);

        return studentService.saveStudent(student);
    }

}
