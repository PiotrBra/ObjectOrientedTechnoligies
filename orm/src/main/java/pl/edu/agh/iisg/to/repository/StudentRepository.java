package pl.edu.agh.iisg.to.repository;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.service.SchoolService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public class StudentRepository implements Repository<Student>{

    private final StudentDao studentDao;

    private final CourseDao courseDao;

    public StudentRepository(StudentDao studentDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    @Override
    public Optional<Student> add(Student student) {
        return studentDao.save(student);
    }

    @Override
    public Optional<Student> getById(int id) {
        return studentDao.findById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void remove(Student student) {
        for(Course course : student.courseSet()){
            course.studentSet().remove(student);
            courseDao.save(course);
        }

        studentDao.remove(student);

    }

    public List<Student> findAllByCourseName(String courseName) {
        Course course =  courseDao.findByName(courseName).orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return course.studentSet().stream().toList();

    }

    public Optional<Student> getByIndexNumber(int indexNumber){
        return studentDao.findByIndexNumber(indexNumber);

    }
}
