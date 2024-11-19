package app;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
        try {
            photoDownloader.getPhotoExamples().subscribe(photoSerializer::savePhoto);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);
        }
    }

    public void downloadPhotosForQuery(String query) throws IOException {
        try {
            photoDownloader.searchForPhotos(query).subscribe(photoSerializer::savePhoto);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadPhotosForMultipleQueries(List<String> queries) {
        try {
            photoDownloader.searchForPhotos(queries)
                    .compose(this::processPhotosAdvanced)
                    .take(50)
                    .blockingSubscribe(photoSerializer::savePhoto,
                            error -> log.log(Level.SEVERE, "Downloading photo error", error),
                            () -> log.log(Level.SEVERE, "Downloading photo completed"));

        } catch (IOException e) {
            log.log(Level.SEVERE, "Downloading photo examples error", e);

        }
    }

    public Observable<Photo> processPhotos(Observable<Photo> photos) {
        return photos.filter(photoProcessor::isPhotoValid).map(photoProcessor::convertToMiniature);
    }

    public Observable<Photo> processPhotosAdvanced(Observable<Photo> photos) {
        return photos
                .filter(photoProcessor::isPhotoValid)
                .groupBy(photoProcessor::isPhotoMedium).flatMap(groupedObservable -> {
                    if (Boolean.TRUE.equals(groupedObservable.getKey())) {
                        return groupedObservable
                                .buffer(5, TimeUnit.SECONDS)
                                .doOnNext(group->log.log(Level.SEVERE, "Nowy buffor"))
                                .flatMap(Observable::fromIterable);
                    } else {
                        return groupedObservable
                                .observeOn(Schedulers.computation())
                                .map(photoProcessor::convertToMiniature);
                    }
                });
    }

}
