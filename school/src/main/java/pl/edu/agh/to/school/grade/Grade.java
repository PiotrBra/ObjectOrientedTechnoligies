package pl.edu.agh.to.school.grade;

import jakarta.persistence.*;
import pl.edu.agh.to.school.course.Course;

@Entity
public class Grade {
    @Id
    @GeneratedValue
    private int id;

    private int gradeValue;



    @ManyToOne
    private Course course;

    public Grade(int gradeValue, Course course) {
        this.gradeValue = gradeValue;
        this.course = course;
    }

    public Grade() {

    }
    public int getGradeValue() {
        return gradeValue;
    }

    public Course getCourse() {
        return course;
    }

    public void setGradeValue(int gradeValue) {
        this.gradeValue = gradeValue;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
