package pl.edu.agh.to.school.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {


    @Query("SELECT AVG(g.gradeValue) FROM Grade g WHERE g.course.id = :courseId")
    Double calculateAverageGradeForCourse(@Param("courseId") int courseId);
}
