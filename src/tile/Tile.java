package tile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;

    public void loadImage(java.io.InputStream input) {
        try {
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}