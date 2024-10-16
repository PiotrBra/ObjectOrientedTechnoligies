package pl.edu.agh.iisg.to.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import pl.edu.agh.iisg.to.executor.QueryExecutor;

public class Course {

    public static final String TABLE_NAME = "course";

    private static final Logger logger = Logger.getGlobal();

    private final int id;

    private final String name;

    private List<Student> enrolledStudents;

    private boolean isStudentsListDownloaded = false;

    Course(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static Optional<Course> create(final String name) {
        String insertSql = "INSERT INTO course (name) VALUES (?);";
        Object[] args = {
                name
        };

        try {
            int id = QueryExecutor.createAndObtainId(insertSql, args);
            return Course.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Course> findById(final int id) {
        String findByIdSql = "SELECT * FROM course WHERE id = ?";
        Object[] args = {
                id
        };

        try (ResultSet rs = QueryExecutor.read(findByIdSql, args)) {
            if (rs.next()) {
                return Optional.of(new Course(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean enrollStudent(final Student student) {
        // check if student is already enrolled
        String checkIfEnrolledSql = "SELECT COUNT(*) FROM student_course WHERE student_id = ? AND course_id = ?";
        Object[] checkArgs = {
                student.id(),
                this.id
        };

        try (ResultSet rs = QueryExecutor.read(checkIfEnrolledSql, checkArgs)) {
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        // enroll student
        String enrollStudentSql = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";
        Object[] enrollArgs = {
                student.id(),
                this.id
        };

        try {
            int id = QueryExecutor.createAndObtainId(enrollStudentSql, enrollArgs);
            return id > 0; // Zwraca true, jeśli udało się dodać studenta do kursu
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> studentList() {
        String getStudentsSql = "SELECT s.id, s.first_name, s.last_name, s.index_number " +
                "FROM student s " +
                "JOIN student_course sc ON s.id = sc.student_id " +
                "WHERE sc.course_id = ?";
        Object[] args = { this.id };

        List<Student> students = new LinkedList<>();

        try (ResultSet rs = QueryExecutor.read(getStudentsSql, args)) {
            while (rs.next()) {
                int studentId = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int indexNumber = rs.getInt("index_number");

                Student student = new Student(studentId, firstName, lastName, indexNumber);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public List<Student> cachedStudentsList() {
        // check if list of student is downloaded
        if (!isStudentsListDownloaded) {
            enrolledStudents = studentList();
            isStudentsListDownloaded = true;
        }

        return enrolledStudents;
    }


    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String NAME = "name";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        return name.equals(course.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
}
