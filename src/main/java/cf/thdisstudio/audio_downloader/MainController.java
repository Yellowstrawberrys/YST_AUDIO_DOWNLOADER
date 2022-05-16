package cf.thdisstudio.audio_downloader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {
    @FXML
    public Slider playBar;

    @FXML
    public AnchorPane PlayerPanel;

    @FXML
    public Label label_current;

    @FXML
    public Label label_end;

    @FXML
    public ImageView background_img;

    @FXML
    public TextField urlBar;

    @FXML
    public AnchorPane playlistPanel;

    @FXML
    public ScrollPane playListDisplay;

    @FXML
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AudioDownloaderApplication.class.getResource("Video.fxml"));
        playlistPanel.getChildren().add(fxmlLoader.load());
        ((Video) fxmlLoader.getController()).videoURL = "https://www.youtube.com/watch?v=jNQXAC9IVRw";
        ((Video) fxmlLoader.getController()).init();
        //new AudioPlayerManager().play("file:///Users/Yellowstrawberry/audioDownloader/NoMorE.mp3");
        background_img.setPreserveRatio(true);
        urlBar.setLayoutX(0);
        PlayerPanel.widthProperty().addListener((obs, oldVal, newVal) -> {
            background_img.setFitWidth(PlayerPanel.getWidth());
            background_img.setFitHeight(PlayerPanel.getHeight());
            playBar.setPrefSize(PlayerPanel.getWidth()-100, 10);
            playBar.setLayoutX(50);
            label_current.setLayoutX(35);
            label_end.setLayoutX(PlayerPanel.getWidth()-65);
            urlBar.setPrefSize(playlistPanel.getWidth(), 25);
            playListDisplay.setPrefSize(playlistPanel.getWidth(), playlistPanel.getHeight());
        });

        PlayerPanel.heightProperty().addListener((obs, oldVal, newVal) -> {
            playListDisplay.setPrefSize(playlistPanel.getWidth(), playlistPanel.getHeight());
        });
    }
}