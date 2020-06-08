package git.arcanesunku.engine.gfx.images;

public class ImageTile extends Image {

    private int m_TileWidth, m_TileHeight;

    public ImageTile(String path, int tileWidth, int tileHeight) {
        super(path);

        m_TileWidth = tileWidth;
        m_TileHeight = tileHeight;
    }

    public Image getTileImage(int tileX, int tileY) {
        int[] pixels = new int[m_TileWidth * m_TileHeight];

        for(int y = 0; y < m_TileHeight; y++) {
            for(int x = 0; x < m_TileWidth; x++) {
                pixels[x + y * m_TileWidth] = getPixels()[(x + tileX * m_TileWidth) + (y + tileY * m_TileHeight) * getWidth()];
            }
        }

        return new Image(pixels, m_TileWidth, m_TileHeight);
    }

    public int getTileWidth() {
        return m_TileWidth;
    }

    public void setTileWidth(int tileWidth) {
        m_TileWidth = tileWidth;
    }

    public int getTileHeight() {
        return m_TileHeight;
    }

    public void setTileHeight(int tileHeight) {
        m_TileHeight = tileHeight;
    }
}
