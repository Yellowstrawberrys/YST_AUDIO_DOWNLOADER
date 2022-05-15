package cf.thdisstudio.audio_downloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AudioDownloaderApplication extends Application {

    public static File tmp = new File(System.getProperty("java.io.tmpdir")+"/audioDownloader");
    public static File downloadFolder = new File(System.getProperty("user.home")+"/audioDownloader/");
    public static FXMLLoader fxmlLoader = new FXMLLoader(AudioDownloaderApplication.class.getResource("Main.fxml"));

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(fxmlLoader.load(), 678, 400);
        stage.setTitle("YST Audio Downloader");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}