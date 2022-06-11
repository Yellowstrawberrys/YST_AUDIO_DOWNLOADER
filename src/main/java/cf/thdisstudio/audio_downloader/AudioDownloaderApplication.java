package cf.thdisstudio.audio_downloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class AudioDownloaderApplication extends Application {

    public static File tmp = new File(System.getProperty("java.io.tmpdir")+"/audioDownloader");
    public static File downloadFolder = new File(System.getProperty("user.home")+"/audioDownloader/");
    public static FXMLLoader fxmlLoader = new FXMLLoader(AudioDownloaderApplication.class.getResource("Main.fxml"));
    public static AudioPlayerManager audioPlayerManager = new AudioPlayerManager();

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(fxmlLoader.load(), 678, 400);
        stage.getIcons().add(new Image(AudioDownloaderApplication.class.getResourceAsStream("img/YSMDLogo.png")));
        stage.setTitle("YSMD");
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(169.5);
        stage.setMinHeight(100);
        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> System.exit(0));
    }

    public static void main(String[] args) {
        launch();
    }

}