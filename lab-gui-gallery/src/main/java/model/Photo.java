package model;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import javafx.beans.property.*;
import javafx.scene.image.Image;


public class Photo {

    private final StringProperty name;

    private final ObjectProperty<Image> photoData;

    public Photo(String extension, byte[] photoData) {
        this.photoData = new SimpleObjectProperty<Image>(new Image(new ByteArrayInputStream(photoData)));
        this.name = new SimpleStringProperty(UUID.randomUUID() + "." + extension);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Image getPhotoData() {
        return photoData.get();
    }
    public ReadOnlyObjectProperty<Image> photoDataProperty() {
        return photoData;
    }
}
