package git.arcanesunku.engine.gfx;

import git.arcanesunku.engine.gfx.images.Image;

public class Font {
    private final int m_CharOffset;

    private Image m_FontImage;
    private int[] m_Offsets, m_Widths;

    public Font(String path) {
        this(path, 256, 0);
    }

    public Font(String path, int maxChars, int charOffset) {
        m_FontImage = new Image("fonts/" + path);

        m_Offsets = new int[maxChars];
        m_Widths = new int[maxChars];
        m_CharOffset = charOffset;

        int unicode = 0;

        for(int i = 0; i < m_FontImage.getWidth(); i++) {
            if(m_FontImage.getPixels()[i] == 0xff0000ff) {
                m_Offsets[unicode] = i;
            }

            if(m_FontImage.getPixels()[i] == 0xffffff00) {
                m_Widths[unicode] = i - m_Offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getFontImage() {
        return m_FontImage;
    }

    public void setFontImage(Image fontImage) {
        m_FontImage = fontImage;
    }

    public int[] getOffsets() {
        return m_Offsets;
    }

    public void setOffsets(int[] offsets) {
        m_Offsets = offsets;
    }

    public int[] getWidths() {
        return m_Widths;
    }

    public void setWidths(int[] widths) {
        m_Widths = widths;
    }

    public int getCharOffset() {
        return m_CharOffset;
    }
}
