package cf.thdisstudio.audio_downloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AudioDownloaderApplication extends Application {

    public static Connection conn = null;
    public static File conf = new File(System.getProperty("user.home")+"/audioDownloader/conf/");
    public static File caches = new File(System.getProperty("user.home")+"/audioDownloader/caches/");
    public static File downloadFolder = new File(System.getProperty("user.home")+"/audioDownloader/downloads/");
    public static FXMLLoader fxmlLoader = new FXMLLoader(AudioDownloaderApplication.class.getResource("Main.fxml"));
    public static AudioPlayerManager audioPlayerManager = new AudioPlayerManager();

    @Override
    public void start(Stage stage) {
        try {
            if (!conf.exists())
                conf.mkdirs();
            if (!caches.exists())
                caches.mkdirs();
            if (!new File(conf + "/data.sqlite").exists())
                Files.copy(AudioDownloaderApplication.class.getResourceAsStream("dev/conf/data.sqlite"), new File(conf + "/data.sqlite").toPath(), StandardCopyOption.REPLACE_EXISTING);
            conn = DriverManager.getConnection("jdbc:sqlite:"+ new File(conf + "/data.sqlite").toPath());
            Scene scene = new Scene(fxmlLoader.load(), 678, 400);
            stage.getIcons().add(new Image(AudioDownloaderApplication.class.getResourceAsStream("img/YSMDLogo.png")));
            stage.setTitle("YSMD");
            stage.setScene(scene);
            stage.show();
            stage.setMinWidth(169.5);
            stage.setMinHeight(100);
            scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> System.exit(0));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    static String format = """
            {
                "id": "%s",
                "title": "%s",
                "uploader": "%s"
            }
            """;

    public static void addVideo(File path, String id, String title, String uploader, String url) throws SQLException {
        if(!conn.createStatement().executeQuery("SELECT url FROM videos WHERE url='"+url+"';").next())
            conn.createStatement().executeUpdate("INSERT INTO videos(url, path, data) VALUES('"+url+"', '"+path+"', '"+format.formatted(id, URLEncoder.encode(title, StandardCharsets.UTF_8), URLEncoder.encode(uploader, StandardCharsets.UTF_8))+"');");
    }

}