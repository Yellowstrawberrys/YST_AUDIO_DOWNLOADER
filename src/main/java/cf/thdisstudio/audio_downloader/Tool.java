package cf.thdisstudio.audio_downloader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Tool {
    public static BufferedImage cropImage(BufferedImage thumbnail, int w, int h, int x, int y) {
        return thumbnail.getSubimage(x, y, w, h);
    }

    public static InputStream getImgFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", RandomUserAgent.getRandomUserAgent());
            connection.connect();

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
            return new ByteArrayInputStream(os.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
