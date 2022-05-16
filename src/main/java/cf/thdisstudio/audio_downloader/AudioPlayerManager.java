package cf.thdisstudio.audio_downloader;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class AudioPlayerManager {

    public Video video;
    MediaPlayer mediaPlayer;

    public void play(Video video){
        this.video = video;
        if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
            mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(new Media(video.target.toURI().toString()));
        MainController mainController = AudioDownloaderApplication.fxmlLoader.getController();
        mainController.background_img.setImage(video.thumbnail);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.7);
        mainController.background_img.setEffect(colorAdjust);
        mainController.label_title.setText(video.title);
        mainController.label_author.setText(video.uploader);
        mediaPlayer.currentTimeProperty().addListener((ovl, oldVal, newVal) -> {
            if(!mainController.playBar.isValueChanging()) {
                double total = mediaPlayer.getTotalDuration().toSeconds();
                double current = mediaPlayer.getCurrentTime().toSeconds();
                mainController.playBar.setValue((current / total) * 100);
                mainController.label_current.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.SECONDS.toHours((long) current) -
                                TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays((long) current)), // The change is in this line
                        TimeUnit.SECONDS.toMinutes((long) current) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long) current)), // The change is in this line
                        TimeUnit.SECONDS.toSeconds((long) current) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long) current))));
                mainController.label_end.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.SECONDS.toHours((long) total) -
                                TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays((long) total)), // The change is in this line
                        TimeUnit.SECONDS.toMinutes((long) total) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours((long) total)), // The change is in this line
                        TimeUnit.SECONDS.toSeconds((long) total) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes((long) total))));
            }
        });
        mediaPlayer.play();
    }

    public void play(){
        mediaPlayer.play();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void set(double pos){
        if(mediaPlayer != null)
            mediaPlayer.seek(Duration.millis(mediaPlayer.getTotalDuration().toMillis()*pos));
    }
}
