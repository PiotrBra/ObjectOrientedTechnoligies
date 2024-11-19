package pl.edu.agh.iisg.to.dao;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDao extends GenericDao<Student> {

    public StudentDao(SessionService sessionService) {
        super(sessionService, Student.class);
    }

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        // TODO - implement
        Student student = new Student(firstName, lastName, indexNumber);
        return save(student);
    }

    public List<Student> findAll() {
        // TODO - implement
        Session session = currentSession();
        List<Student> students = session.createQuery("Select s from Student s order by s.lastName").list();
        return students;
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        // TODO - implement

        try{
            return currentSession().createQuery("Select s from Student s where s.indexNumber = :indexNumber", Student.class)
                    .setParameter("indexNumber", indexNumber).uniqueResultOptional();

        }catch (PersistenceException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
