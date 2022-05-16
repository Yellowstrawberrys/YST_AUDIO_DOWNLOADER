package cf.thdisstudio.audio_downloader;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.YoutubeProgressCallback;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.Format;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.io.IOException;

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

    public VideoInfo info;
    public String title;
    public String id;
    public String videoURL;
    public String uploader;
    public Image thumbnail;
    public File target;

    public String status;
    Loader loader;
    YoutubeDownloader downloader = new YoutubeDownloader();

    double beforeHeight = 0;

    public void init() throws IOException {
        label_title.setStyle("-fx-font: 18 \"Noirden Bold\";");
        label_title.setLayoutX(54);
        label_title.setLayoutY(10);
        label_author.setStyle("-fx-font: 8 \"Noirden Bold\";");
        label_author.setLayoutX(53);
        label_author.setLayoutY(30);
        thum.setFitHeight(40);
        thum.setFitWidth(40);
        thum.setLayoutX(10);
        loader = new Loader();
        info = loader.getVideoInfo(videoURL.split("\\?v=")[1]);
        title = info.details().title();
        videoURL = "https://youtube.com/watch?v="+info.details().videoId();
        uploader = info.details().author();
        id = info.details().videoId();
        thumbnail = new Image(String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", id));
        label_title.setText(title);
        label_author.setText(uploader);
        thum.setImage(new WritableImage(thumbnail.getPixelReader(), (int) (thumbnail.getWidth()/4), 0, (int) thumbnail.getHeight(), (int) thumbnail.getHeight()));
        target = new File(downloadFolder+"/"+title.replaceAll("[^a-zA-Z0-9\\.\\-]", "_")+".m4a");
        System.out.println("Finished Loading Things");
        loader.start();
    }

    public void onMouseClicked(){
        if(!loader.disabled){
            audioPlayerManager.play(this);
        }
    }

    int width = 0;
    int height = 0;
    boolean isEntered = false;

    class Loader extends Thread{
        boolean disabled = true;
        public void run(){
            try {
                if(!target.exists()) {
                    // get videos formats only with audio
                    status = "Downloading";
                    File outputDir = new File(downloadFolder+"/");
                    Format format = info.bestAudioFormat();
                    RequestVideoFileDownload request = new RequestVideoFileDownload(format)
                            .saveTo(outputDir)
                            .renameTo(title.replaceAll("[^a-zA-Z0-9\\.\\-]", "_"))
                            .callback(new YoutubeProgressCallback<>() {
                                @Override
                                public void onDownloading(int progress) {
                                    System.out.printf("Downloaded %d%%\n", progress);
                                }

                                @Override
                                public void onFinished(File videoInfo) {
                                    System.out.println("Finished file: " + videoInfo);
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
                disabled = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public VideoInfo getVideoInfo(String videoId){
            YoutubeDownloader downloader = new YoutubeDownloader();
// async parsing
            RequestVideoInfo request = new RequestVideoInfo(videoId)
                    .callback(new YoutubeCallback<VideoInfo>() {
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

        public void convert() throws Exception{
            status = "Converting";
            File source = new File(AudioDownloaderApplication.tmp+"/"+title
                    .replaceAll("<", "")
                    .replaceAll(">", "")
                    .replaceAll(":", "")
                    .replaceAll("\"", "")
                    .replaceAll("/", "")
                    .replaceAll("\\\\", "")
                    .replaceAll("\\|", "")
                    .replaceAll("\\?", "")
                    .replaceAll("\\*", "")+".mp4");

            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp3");
            attrs.setAudioAttributes(audio);


            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);

            //Delete
            source.delete();

            disabled = false;
            status = "Finished";
            System.out.println(status);
        }
    }
}
