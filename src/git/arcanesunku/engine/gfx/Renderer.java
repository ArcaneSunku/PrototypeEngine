package git.arcanesunku.engine.gfx;

import git.arcanesunku.engine.GameContainer;
import git.arcanesunku.engine.gfx.images.Image;
import git.arcanesunku.engine.gfx.images.ImageRequest;
import git.arcanesunku.engine.gfx.images.ImageTile;
import git.arcanesunku.engine.gfx.lights.Light;
import git.arcanesunku.engine.gfx.lights.LightRequest;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Renderer {
    private final ArrayList<ImageRequest> m_ImageRequests;
    private final ArrayList<LightRequest> m_LightRequests;

    private final int[] m_Pixels;
    private final int[] m_ZBuffer;

    private final int[] m_LightMap;
    private final int[] m_LightBlock;

    private final int m_ClearColor;
    private final int m_pixWidth, m_pixHeight;

    private Font m_Font;

    private int m_ZDepth = 0;
    private int m_AmbientColor;

    private boolean m_SunPresent;
    private boolean m_Processing;

    public Renderer(GameContainer gc) {
        m_ImageRequests = new ArrayList<>();
        m_LightRequests = new ArrayList<>();

        m_ClearColor = 0xff000000;
        m_AmbientColor = 0xff232323;

        m_pixWidth = gc.getWidth();
        m_pixHeight = gc.getHeight();
        m_Pixels = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();

        m_ZBuffer = new int[m_Pixels.length];
        m_LightMap = new int[m_Pixels.length];
        m_LightBlock = new int[m_Pixels.length];

        m_SunPresent = true;
    }

    public void clear() {
        Arrays.fill(m_Pixels, m_ClearColor);
        Arrays.fill(m_ZBuffer, m_ClearColor);

        if(m_SunPresent) {
            m_AmbientColor = 0xffcccccc;
        } else {
            m_AmbientColor = 0xff232323;
        }

        Arrays.fill(m_LightMap, m_AmbientColor);
        Arrays.fill(m_LightBlock, m_AmbientColor);
    }

    public void process() {
        // ZDepth Testing
        m_Processing = true;
        m_ImageRequests.sort(Comparator.comparingInt(a -> a.zDepth));

        // Adjust their zDepth and Render them accordingly
        for (ImageRequest request : m_ImageRequests) {
            setZDepth(request.zDepth);
            drawImage(request.image, request.offX, request.offY);
        }

        // Mixes the light colors with all of the colors in our screen, then draws it

        for(LightRequest request : m_LightRequests) {
            drawLightRequest(request.light, request.offX, request.offY);
        }

        for(int i = 0; i < m_Pixels.length; i++) {
            float r = ((m_LightMap[i] >> 16) & 0xff) / 255f;
            float g = ((m_LightMap[i] >> 8) & 0xff) / 255f;
            float b = (m_LightMap[i] & 0xff) / 255f;

            m_Pixels[i] = ((int) (((m_Pixels[i] >> 16) & 0xff) * r) << 16 | (int) (((m_Pixels[i] >> 8) & 0xff) * g) << 8 | (int) ((m_Pixels[i] & 0xff) * b));
        }

        m_ImageRequests.clear();
        m_LightRequests.clear();

        m_Processing = false;
    }

    private void setPixel(int x, int y, int value) {
        int alpha = (value >> 24) & 0xff;

        if((x < 0 || x >= m_pixWidth || y < 0 || y >= m_pixHeight) || alpha == 0)
            return;

        int index = x + y * m_pixWidth;

        if(m_ZBuffer[index] > m_ZDepth)
            return;

        m_ZBuffer[index] = m_ZDepth;

        if(alpha == 255) {
            m_Pixels[index] = value;
        } else {
            int pixelColor = m_Pixels[index];

            int newRed = ((pixelColor >> 16) & 0xff) - (int) ((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int) ((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor & 0xff) - (int) (((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));

            m_Pixels[index] = (newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    public void setLightMap(int x, int y, int value) {
        if(x < 0 || x >= m_pixWidth || y < 0 || y >= m_pixHeight)
            return;

        int baseColor = m_LightMap[x + y * m_pixWidth];

        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max(baseColor & 0xff, value & 0xff);

        m_LightMap[x + y * m_pixWidth] = (maxRed << 16 | maxGreen << 8 | maxBlue);
    }

    public void setLightBlock(int x, int y, int value) {
        if(x < 0 || x >= m_pixWidth || y < 0 || y >= m_pixHeight)
            return;

        if(m_ZBuffer[x + y * m_pixWidth] > m_ZDepth)
            return;

        m_LightBlock[x + y * m_pixWidth] = value;
    }

    public void fillRect(int offX, int offY, int width, int height, int color) {
        // Render Check
        if(offX < -width) return;
        if(offY < -height) return;

        if(offX >= m_pixWidth) return;
        if(offY >= m_pixHeight) return;

        // Init Variables
        int newX = 0, newY = 0;
        int newWidth = width, newHeight = height;

        // Clipping
        if(offX <= 0) newX -= offX;
        if(offY <= 0) newY -= offY;

        if(newWidth + offX >= m_pixWidth) newWidth -= (newWidth + offX - m_pixWidth);
        if(newHeight + offY >= m_pixHeight) newHeight -= (newHeight + offY - m_pixHeight);


        for(int y = newY; y < newHeight; y++) {
            for(int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, color);
            }
        }
    }

    public void drawRect(int offX, int offY, int width, int height, int color) {
        for(int y = 0; y <= height; y++) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }

        for(int x = 0; x <= width; x++) {
            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
        }
    }

    public void drawText(String text, int offX, int offY, int color) {
        drawText(text, offX, offY, color, false);
    }

    public void drawText(String text, int offX, int offY, int color, boolean toUpper) {
        if(m_Font == null) return;

        Image fontImage = m_Font.getFontImage();

        if(toUpper)
            text = text.toUpperCase();

        int offset = 0;

        for(int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - m_Font.getCharOffset();

            for(int y = 0; y < fontImage.getHeight(); y++) {
                for(int x = 0; x < m_Font.getWidths()[unicode]; x++) {
                    if(fontImage.getPixels()[(x + m_Font.getOffsets()[unicode]) + y * fontImage.getWidth()] == 0xffffffff) {
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }

            offset += m_Font.getWidths()[unicode];
        }
    }

    public void drawImage(Image image, int offX, int offY) {
        if(image.hasAlpha() && !m_Processing) {
            m_ImageRequests.add(new ImageRequest(image, m_ZDepth, offX, offY));
            return;
        }

        // Render Check
        if(offX < -image.getWidth()) return;
        if(offY < -image.getHeight()) return;

        if(offX >= m_pixWidth) return;
        if(offY >= m_pixHeight) return;

        // Init Variables
        int newX = 0, newY = 0;
        int newWidth = image.getWidth(), newHeight = image.getHeight();

        // Clipping
        if(offX <= 0) newX -= offX;
        if(offY <= 0) newY -= offY;

        if(newWidth + offX >= m_pixWidth) newWidth -= (newWidth + offX - m_pixWidth);
        if(newHeight + offY >= m_pixHeight) newHeight -= (newHeight + offY - m_pixHeight);

        // Rendering
        for(int y = newY; y < newHeight; y++) {
            for(int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, image.getPixels()[x + y * image.getWidth()]);
                    int alpha = (image.getPixels()[x + y * image.getWidth()] >> 24) & 0xff;

                    if(alpha == 0) {
                        setLightBlock(x + offX, y + offY, 0);
                    } else {
                        setLightBlock(x + offX, y + offY, image.getLightBlock());
                    }
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {
        if(image.hasAlpha() && !m_Processing) {
            m_ImageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY), m_ZDepth, offX, offY));
            return;
        }

        // Render Check
        if(offX < -image.getTileWidth()) return;
        if(offY < -image.getTileHeight()) return;

        if(offX >= m_pixWidth) return;
        if(offY >= m_pixHeight) return;

        // Init Variables
        int newX = 0, newY = 0;
        int newWidth = image.getTileWidth(), newHeight = image.getTileHeight();

        // Clipping
        if(offX <= 0) newX -= offX;
        if(offY <= 0) newY -= offY;

        if(newWidth + offX >= m_pixWidth) newWidth -= (newWidth + offX - m_pixWidth);
        if(newHeight + offY >= m_pixHeight) newHeight -= (newHeight + offY - m_pixHeight);

        // Rendering
        for(int y = newY; y < newHeight; y++) {
            for(int x = newX; x < newWidth; x++) {
                setPixel(x + offX, y + offY, image.getPixels()[(x + tileX * image.getTileWidth()) + (y + tileY * image.getTileHeight()) * image.getWidth()]);
                setLightBlock(x + offX, y + offY, image.getLightBlock());
            }
        }
    }

    public void drawLight(Light l, int offX, int offY) {
        m_LightRequests.add(new LightRequest(l, offX, offY));
    }

    private void drawLightRequest(Light l, int offX, int offY) {
        for(int i = 0; i <= l.getDiameter(); i++) {
            drawLightLine(l, l.getRadius(), l.getRadius(), i, 0, offX, offY); // Top beam
            drawLightLine(l, l.getRadius(), l.getRadius(), i, l.getDiameter(), offX, offY); // Bottom beam
            drawLightLine(l, l.getRadius(), l.getRadius(), 0, i, offX, offY); // Left beam
            drawLightLine(l, l.getRadius(), l.getRadius(),l.getDiameter(), i, offX, offY); // Right beam
        }
    }

    private void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offX, int offY) {
        int dX = Math.abs(x1 - x0);
        int dY = Math.abs(y1 - y0);

        // Helps us decide where our line is going
        int sX = x0 < x1 ? 1 : -1;
        int sY = y0 < y1 ? 1 : -1;

        int err = dX - dY;
        int err1;

        while(true) {
            int screenX = x0 - l.getRadius() + offX;
            int screenY = y0 - l.getRadius() + offY;
            int lightColor = l.getLightValue(x0, y0);

            if(lightColor == 0)
                return;

            if(screenX < 0 || screenX >= m_pixWidth || screenY < 0 || screenY >= m_pixHeight)
                return;

            // Sun should be like a 'universal' lighting of sorts.
            if(l.isSun()) {
                continue;
            } else {

                // If our light line hits a 'solid' pixel, it stops
                if (m_LightBlock[screenX + screenY * m_pixWidth] == Light.FULL)
                    return;
            }

            setLightMap(screenX, screenY, lightColor);

            // Literally no need to re-do math for lines that are exactly the same, so we dont
            if(x0 == x1 && y0 == y1)
                break;

            // Otherwise we'll create the line for the light
            err1 = 2 * err;

            if(err1 > -1 * dY) {
                err -= dY;
                x0 += sX;
            }

            if(err1 < dX) {
                err += dX;
                y0 += sY;
            }
        }
    }

    public void setSunPresent(boolean sunPresent) {
        m_SunPresent = sunPresent;
    }

    public boolean isSunPresent() {
        return m_SunPresent;
    }

    public void setZDepth(int zDepth) {
        m_ZDepth = zDepth;
    }

    public int getZDepth() {
        return m_ZDepth;
    }

    public void setFont(String font) {
        m_Font = new Font(font);
    }

    public void setFont(Font font) {
        m_Font = font;
    }

}
