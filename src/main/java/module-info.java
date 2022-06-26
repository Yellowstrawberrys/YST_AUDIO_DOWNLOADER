module cf.thdisstudio.audio_downloader {
    requires java.sql;
    requires java.xml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.controls;
    requires java.youtube.downloader;
    requires org.json;

    exports cf.thdisstudio.audio_downloader;
    opens cf.thdisstudio.audio_downloader to javafx.graphics;
}