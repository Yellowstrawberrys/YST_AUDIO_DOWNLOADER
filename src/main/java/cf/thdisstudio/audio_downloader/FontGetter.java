package cf.thdisstudio.audio_downloader;


import javafx.scene.text.Font;

import java.io.InputStream;

public class FontGetter {
    public static Font customFont(String name, double size){
        try {
            InputStream myStream = null;
            try {
                myStream = FontGetter.class.getResourceAsStream("/cf/thdisstudio/audio_downloader/font/" +name+".ttf");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Font.loadFont(myStream, size);
        }catch (Exception e){
            return null;
        }
    }
}
