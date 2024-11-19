package controller;


import com.sun.scenario.effect.impl.prism.PrImage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Gallery;
import model.Photo;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import util.PhotoDownloader;


public class GalleryController {

    @FXML
    private ImageView imageView;

    @FXML
    private TextField imageNameField;

    @FXML
    private ListView<Photo> imagesListView;

    @FXML
    private TextField searchTextField;

    private Gallery galleryModel;

    @FXML
    public void initialize() {
        imagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView photoIcon = new ImageView(item.getPhotoData());
                    photoIcon.setPreserveRatio(true);
                    photoIcon.setFitHeight(50);
                    setGraphic(photoIcon);
                }
            }
        });

        imagesListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> { bindSelectedPhoto(newValue); }
        );
    }

    public void setModel(Gallery gallery) {
        this.galleryModel = gallery;
        bindSelectedPhoto(gallery.getPhotos().get(0));
        imagesListView.setItems(gallery.getPhotos());
    }

    public void setImageNameField(TextField imageNameField) {
        this.imageNameField = imageNameField;
    }

    private void bindSelectedPhoto(Photo selectedPhoto) {
        if (selectedPhoto != null) {
            // Odlaczanie poprzedniego
            if (imageNameField.textProperty().isBound()) {
                imageNameField.textProperty().unbindBidirectional(selectedPhoto.nameProperty());
            }

            // Dwukierunkowe powiązanie
            imageNameField.textProperty().bindBidirectional(selectedPhoto.nameProperty());
            imageView.imageProperty().bind(selectedPhoto.photoDataProperty());
        }
    }

    public void searchButtonClicked(ActionEvent event) {
        String query = searchTextField.getText();
        PhotoDownloader photoDownloader = new PhotoDownloader();
        galleryModel.clear();
        Observable<Photo> photoObservable = photoDownloader.searchForPhotos(query);

        photoObservable
                .subscribeOn(Schedulers.io()) // Praca w tle
                .observeOn(JavaFxScheduler.platform()) // Operacje w głównym wątku
                .subscribe(
                        photo -> {
                            galleryModel.addPhoto(photo);
                        },
                        error -> {
                            System.err.println("Error occurred while downloading photos: " + error.getMessage());
                        }
                );
    }
}

