package pl.edu.agh.to.school.student;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.grade.Grade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String indexNumber;

    @OneToMany
    private List<Grade> grades = new ArrayList<Grade>();

    public Student( String firstName, String lastName, LocalDate birthDate, String indexNumber) {;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.indexNumber = indexNumber;
    }

    public Student() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public void giveGrade(Grade grade){
        grades.add(grade);
    }

    public Double calculateAverageGrade() {
        if (grades.isEmpty()) {
            return null;
        }


        double total = grades.stream()
                .mapToDouble(Grade::getGradeValue)
                .sum();

        return total / grades.size();
    }

    public List<Grade> getGrades() {
        return grades;
    }
}