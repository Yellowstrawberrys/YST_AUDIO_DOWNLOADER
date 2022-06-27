package cf.thdisstudio.audio_downloader;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.YoutubeProgressCallback;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.Format;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static cf.thdisstudio.audio_downloader.AudioDownloaderApplication.audioPlayerManager;
import static cf.thdisstudio.audio_downloader.AudioDownloaderApplication.downloadFolder;

public class Video {

    @FXML
    public Label label_title;
    @FXML
    public Label label_author;
    @FXML
    public ImageView thum;
    @FXML
    public AnchorPane main;
    @FXML
    public ProgressBar downloadProgressBar;

    public VideoInfo info;
    public String title;
    public String id;
    public String videoURL;
    public String uploader;
    public Image thumbnail;
    public File target;
    public String status;
    public Node current;
    Loader loader;
    YoutubeDownloader downloader = new YoutubeDownloader();

    double beforeHeight = 0;

    public void init(boolean isLocal) throws ArrayIndexOutOfBoundsException, IOException, SQLException {
        label_title.setStyle("-fx-font: 18 \"Noirden Bold\";");
        label_title.setLayoutX(54);
        label_title.setLayoutY(10);
        label_author.setStyle("-fx-font: 8 \"Noirden Bold\";");
        label_author.setLayoutX(53);
        label_author.setLayoutY(30);
        thum.setFitHeight(40);
        thum.setFitWidth(40);
        thum.setLayoutX(10);
        main.setPrefSize(403, 50);
        downloadProgressBar.setRotate(180);
        ((FlowPane) main.getParent()).widthProperty().addListener((s1, s2, s3) -> {
            double width = ((FlowPane) main.getParent()).getPrefWidth();
            main.setPrefWidth(width);
            downloadProgressBar.setPrefWidth(width);
        });
        downloadProgressBar.setPrefSize(403, 50);
        loader = new Loader();
        if (!isLocal) {
            info = loader.getVideoInfo(videoURL.split("\\?v=")[1]);
            title = info.details().title();
            target = new File(downloadFolder+"/"+title.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("\\s", "_")+".m4a");
            uploader = info.details().author();
            id = info.details().videoId();
            thumbnail = new Image(String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", id));
            update();
            thumbnail();
            loader.start();
            AudioDownloaderApplication.addVideo(target, id, title, uploader,"https://youtube.com/watch?v="+id);
        }else {
            target = new File(downloadFolder+"/"+title.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("\\s", "_")+".m4a");
            thumbnail = new Image(new File(AudioDownloaderApplication.caches + "/" + id + ".png").toURI().toString());
            thumbnail();
            update();
            loader.loadFin();
        }
    }

    public void onMouseClicked(){
        if(!loader.disabled){
            try {
                audioPlayerManager.play(this);
            }catch (Exception e){
                bypass =true;
                loader = new Loader();
                loader.start();
            }
        }
    }

    public void update(){
        label_title.setText(title);
        label_author.setText(uploader);
        thum.setImage(new WritableImage(thumbnail.getPixelReader(), (int) (thumbnail.getWidth()/4), 0, (int) thumbnail.getHeight(), (int) thumbnail.getHeight()));
    }

    public void thumbnail() {
        new Thread(() -> {
            try {
                if (!new File(AudioDownloaderApplication.caches + "/" + id + ".png").exists()) {
                    InputStream in = new URL(String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", id)).openStream();
                    try {
                        Files.copy(in, Paths.get(AudioDownloaderApplication.caches + "/" + id + ".png"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                thumbnail = new Image(AudioDownloaderApplication.caches + "/" + id + ".png");
            }catch (Exception e){}
        }).start();
    }

    int width = 0;
    int height = 0;
    boolean isEntered = false;
    boolean bypass = false;

    class Loader extends Thread{
        boolean disabled = true;
        public void run(){
            try {
                downloadProgressBar.setVisible(true);
                if(!target.exists() || bypass) {
                    status = "Downloading";
                    File outputDir = new File(downloadFolder+"/");
                    if(info == null)
                        info = loader.getVideoInfo(videoURL.split("\\?v=")[1]);
                    Format format = info.bestAudioFormat();
                    RequestVideoFileDownload request = new RequestVideoFileDownload(format)
                            .saveTo(outputDir)
                            .renameTo(title.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("\\s", "_"))
                            .callback(new YoutubeProgressCallback<>() {
                                @Override
                                public void onDownloading(int progress) {
                                    Platform.runLater(() -> {
                                        label_author.setText("Downloading... (%s%%)".formatted(progress));
                                        downloadProgressBar.setProgress(1d-(((double) progress)/100d));
                                    });
                                }

                                @Override
                                public void onFinished(File videoInfo) {
                                    Platform.runLater(() -> label_author.setText(uploader));
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    System.out.println("Error: " + throwable.getLocalizedMessage());
                                }
                            })
                            .async();
                    Response<File> response = downloader.downloadVideoFile(request);
                    File data = response.data(); // will block current thread
                    //convert();
                }
                loadFin();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public VideoInfo getVideoInfo(String videoId){
            YoutubeDownloader downloader = new YoutubeDownloader();
// async parsing
            RequestVideoInfo request = new RequestVideoInfo(videoId)
                    .callback(new YoutubeCallback<>() {
                        @Override
                        public void onFinished(VideoInfo videoInfo) {
                            System.out.println("Finished parsing");
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            System.out.println("Error: " + throwable.getMessage());
                        }
                    })
                    .async();
            Response<VideoInfo> response = downloader.getVideoInfo(request);
            return response.data();
        }

        public void loadFin(){
            disabled = false;
            downloadProgressBar.setVisible(false);
            main.setOnDragDetected(event -> {
                if(audioPlayerManager.mediaPlayer != null)
                    audioPlayerManager.mediaPlayer.dispose();
                Dragboard db = main.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putFiles(List.of(target));
                db.setContent(content);
                event.consume();
            });
            main.setOnDragDone(event -> {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    loader.stop();
                    ((MainController) AudioDownloaderApplication.fxmlLoader.getController()).hBox.getChildren().remove(current);
                    ((MainController) AudioDownloaderApplication.fxmlLoader.getController()).urls.remove(videoURL);
                    try {
                        AudioDownloaderApplication.conn.createStatement().execute("DELETE FROM videos WHERE url='"+videoURL+"';");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.gc();
                }
                event.consume();
            });
        }
    }
}
