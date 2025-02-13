package pl.edu.agh.school.persistence;

import pl.edu.agh.school.SchoolClass;
import pl.edu.agh.school.Teacher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public interface PersistenceManager {
    public void saveTeachers(List<Teacher> teachers);
    public List<Teacher> loadTeachers();

    public void saveClasses(List<SchoolClass> classes);

    public List<SchoolClass> loadClasses();
}
