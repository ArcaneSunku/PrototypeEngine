package git.arcanesunku.engine.gfx;

public class Color {

    public int alpha, red, green, blue;

    public Color(int color) {
        this((color >> 24) & 0xff, (color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff);
    }

    public Color(int r, int g, int b) {
        this(255, r, g, b);
    }

    public Color(int a, int r, int g, int b) {
        alpha = a & 0xff;
        red = r & 0xff;
        green = g & 0xff;
        blue = b & 0xff;
    }

    public int getColor() {
        return (alpha << 24 | red << 16 | green << 8 | blue);
    }

}
