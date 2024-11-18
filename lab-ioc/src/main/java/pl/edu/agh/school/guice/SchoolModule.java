package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import pl.edu.agh.school.School;
import pl.edu.agh.school.SchoolDAO;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;

public class SchoolModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(String.class)
                .annotatedWith(Names.named("teachersStorageFileName"))
                .toInstance("guiceteachers.dat");

        bind(String.class)
                .annotatedWith(Names.named("classStorageFileName"))
                .toInstance("guiceclasses.dat");
    }

    @Provides
    PersistenceManager providePersistenceManager(SerializablePersistenceManager manager){
        return manager;
    }
}
