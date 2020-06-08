package git.arcanesunku.engine.gfx.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Image {

    private int[] m_Pixels;

    private int m_LightBlock;
    private int m_Width, m_Height;
    private boolean m_Alpha = false;

    public Image(String path) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Image.class.getResourceAsStream("/assets/" + path));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.printf("Failed to load [%s]!%n", path);
            System.exit(-1);
        }

        m_Width = image.getWidth();
        m_Height = image.getHeight();
        m_Pixels = image.getRGB(0, 0, m_Width, m_Height, null, 0, m_Width);

        image.flush();
        m_LightBlock = 0;
    }

    public Image(int[] p, int w, int h) {
        m_Pixels = p;
        m_Width = w;
        m_Height = h;

        m_LightBlock = 0;
    }

    public int getLightBlock() {
        return m_LightBlock;
    }

    public void setLightBlock(int lightBlock) {
        m_LightBlock = lightBlock;
    }

    public boolean hasAlpha() {
        return m_Alpha;
    }

    public void useAlpha(boolean alpha) {
        m_Alpha = alpha;
    }

    public int[] getPixels() {
        return m_Pixels;
    }

    public void setPixels(int[] pixels) {
        m_Pixels = pixels;
    }

    public int getWidth() {
        return m_Width;
    }

    public void setWidth(int width) {
        m_Width = width;
    }

    public int getHeight() {
        return m_Height;
    }

    public void setHeight(int height) {
        m_Height = height;
    }
}
