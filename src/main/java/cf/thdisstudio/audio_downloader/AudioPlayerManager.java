package cf.thdisstudio.audio_downloader;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.concurrent.TimeUnit;

public class AudioPlayerManager {

    MediaPlayer mediaPlayer;

    public void play(String file){
        mediaPlayer = new MediaPlayer(new Media(file));
        mediaPlayer.currentTimeProperty().addListener((ovl, oldVal, newVal) -> {
            MainController mainController = AudioDownloaderApplication.fxmlLoader.getController();

            double total = mediaPlayer.getTotalDuration().toSeconds();
            double current = mediaPlayer.getCurrentTime().toSeconds();
            mainController.playBar.setValue((current/total)*100);
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
        });
        mediaPlayer.play();
    }

    public void play(){
        mediaPlayer.play();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void set(){

    }
}
