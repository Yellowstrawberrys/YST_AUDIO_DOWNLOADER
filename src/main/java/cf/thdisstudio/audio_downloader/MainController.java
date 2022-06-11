package cf.thdisstudio.audio_downloader;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class MainController {

    @FXML
    public SplitPane mainpane;
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

    @FXML
    public Button button_audioTool;

    @FXML
    public ImageView image_audioTool;

    final FlowPane hBox = new FlowPane();

    @FXML
    public void initialize() {
        background_img.setPreserveRatio(false);
        urlBar.setLayoutX(0);
        background_img.fitWidthProperty().bind(PlayerPanel.widthProperty());
        background_img.fitHeightProperty().bind(PlayerPanel.heightProperty());
        playListDisplay.setContent(hBox);
        playListDisplay.setFitToWidth(true);
        playListDisplay.setPannable(true);
        PlayerPanel.widthProperty().addListener((obs, oldVal, newVal) -> {
            playBar.setPrefSize(PlayerPanel.getWidth()-100, 10);
            playBar.setLayoutX(50);
            label_current.setLayoutX(40);
            label_end.setLayoutX(PlayerPanel.getWidth()-65);
            urlBar.setPrefSize(playlistPanel.getWidth(), 25);
            playListDisplay.setPrefSize(playlistPanel.getWidth(), playlistPanel.getHeight());
            button_audioTool.setLayoutX((PlayerPanel.getWidth()-button_audioTool.getPrefWidth())/2);
        });

        playlistPanel.heightProperty().addListener((obs, oldVal, newVal) -> {
            playListDisplay.setPrefHeight(playlistPanel.getHeight()-63);
            hBox.setPrefHeight(playlistPanel.getHeight()-63);
        });

        playlistPanel.widthProperty().addListener((obs, oldVal, newVal) -> {
            playListDisplay.setPrefWidth(playlistPanel.getWidth());
            hBox.setPrefWidth(playlistPanel.getWidth());
        });

        playBar.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if(playBar.isValueChanging() && AudioDownloaderApplication.audioPlayerManager.mediaPlayer != null)
                AudioDownloaderApplication.audioPlayerManager.set(playBar.getValue()/100);
        });
//        image_audioTool.fitHeightProperty().bind(button_audioTool.heightProperty());
//        image_audioTool.fitWidthProperty().bind(button_audioTool.widthProperty());
    }

    public void onClickControlButton(){
        if(AudioDownloaderApplication.audioPlayerManager.mediaPlayer != null && AudioDownloaderApplication.audioPlayerManager.mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
            AudioDownloaderApplication.audioPlayerManager.pause();
        else if(AudioDownloaderApplication.audioPlayerManager.mediaPlayer != null && AudioDownloaderApplication.audioPlayerManager.mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED))
            AudioDownloaderApplication.audioPlayerManager.play();
    }

    public void add() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AudioDownloaderApplication.class.getResource("Video.fxml"));
        Node node = fxmlLoader.load();
        hBox.getChildren().add(node);
        ((Video) fxmlLoader.getController()).videoURL = urlBar.getText();
        urlBar.setText("");
        try {
            ((Video) fxmlLoader.getController()).init();
        }catch (ArrayIndexOutOfBoundsException e){
            hBox.getChildren().remove(node);
        }
    }
}