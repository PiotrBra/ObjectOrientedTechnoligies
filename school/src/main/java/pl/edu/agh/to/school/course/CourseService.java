package pl.edu.agh.to.school.course;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.school.student.Student;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourses(){
        return courseRepository.findAll();
    }

    public List<Student> getStudentsAssignedToCourse(int id){
        return courseRepository.findById(id).get().getStudentList();
    }
    public Optional<Course> getCourseById(int id){
        return courseRepository.findById(id);
    }

    public Double getAverageGradeForCourse(int courseId) {
        return courseRepository.calculateAverageGradeForCourse(courseId);
    }


}
