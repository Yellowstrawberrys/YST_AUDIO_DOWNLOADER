package cf.thdisstudio.audio_downloader;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
    public Label label_title;

    @FXML
    public Label label_author;

    @FXML
    public ImageView background_img;

    @FXML
    public TextField urlBar;

    @FXML
    public AnchorPane playlistPanel;

    @FXML
    public ScrollPane playListDisplay;

    HBox hBox = new HBox();

    @FXML
    public void initialize() throws IOException {
        background_img.setPreserveRatio(false);
        urlBar.setLayoutX(0);
        background_img.fitWidthProperty().bind(PlayerPanel.widthProperty());
        background_img.fitHeightProperty().bind(PlayerPanel.heightProperty());
        playListDisplay.setContent(hBox);
        PlayerPanel.widthProperty().addListener((obs, oldVal, newVal) -> {
            playBar.setPrefSize(PlayerPanel.getWidth()-100, 10);
            playBar.setLayoutX(50);
            label_current.setLayoutX(35);
            label_end.setLayoutX(PlayerPanel.getWidth()-65);
            urlBar.setPrefSize(playlistPanel.getWidth(), 25);
            playListDisplay.setPrefSize(playlistPanel.getWidth(), playlistPanel.getHeight());
        });

        playlistPanel.heightProperty().addListener((obs, oldVal, newVal) -> {
            playListDisplay.setPrefSize(playlistPanel.getWidth(), playlistPanel.getHeight());
        });

        playBar.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if(playBar.isValueChanging() && AudioDownloaderApplication.audioPlayerManager.mediaPlayer != null)
                AudioDownloaderApplication.audioPlayerManager.set(playBar.getValue()/100);
        });
    }

    public void add() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AudioDownloaderApplication.class.getResource("Video.fxml"));
        hBox.getChildren().add(fxmlLoader.load());
        ((Video) fxmlLoader.getController()).videoURL = urlBar.getText();
        urlBar.setText("");
        ((Video) fxmlLoader.getController()).init();
    }
}