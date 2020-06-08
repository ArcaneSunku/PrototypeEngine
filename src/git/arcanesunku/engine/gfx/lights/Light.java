package git.arcanesunku.engine.gfx.lights;

public class Light {

    public static final int NONE = 0;
    public static final int FULL = 1;

    private final int[] m_LightMap;
    private final int m_Radius, m_Diameter, m_Color;

    public Light(int radius, int color) {
        m_Radius = radius;
        m_Color = color;

        m_Diameter = m_Radius * 2;
        m_LightMap = new int[m_Diameter * m_Diameter];

        for(int y = 0; y < m_Diameter; y++) {
            for(int x = 0; x < m_Diameter; x++) {
                double distanceFromCenter = Math.sqrt((x - radius) * (x - radius) + (y - radius) * (y - radius));

                if(distanceFromCenter <= radius) {
                    double power = 1 - (distanceFromCenter / radius);
                    m_LightMap[x + y * m_Diameter] = ((int) (((color >> 16) & 0xff) * power) << 16 | (int) (((color >> 8) & 0xff) * power) << 8 | (int) ((color & 0xff) * power));
                } else {
                    m_LightMap[x + y * m_Diameter] = 0;
                }
            }
        }
    }

    public int[] getLightMap() {
        return m_LightMap;
    }

    public int getLightValue(int x, int y) {
        if(x < 0 || x >= m_Diameter || y < 0 || y >= m_Diameter) return 0;

        return m_LightMap[x + y * m_Diameter];
    }

    public int getRadius() {
        return m_Radius;
    }

    public int getDiameter() {
        return m_Diameter;
    }

    public int getColor() {
        return m_Color;
    }
}
