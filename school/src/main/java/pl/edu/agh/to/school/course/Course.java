package pl.edu.agh.to.school.course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import pl.edu.agh.to.school.student.Student;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {


    @Id
    @GeneratedValue
    private int id;

    private String name;

    public String getName() {
        return name;
    }



    @ManyToMany
    private List<Student> studentList = new ArrayList<Student>();

    public List<Student> getStudentList() {
        return studentList;
    }
    public int getId() {
        return id;
    }
    public Course() {
    }
    public Course(String name) {
        this.name = name;
    }

    public void assignStudent(Student student){
        studentList.add(student);
    }

    public void removeStudent(Student student){
        studentList.remove(student);
    }


}
