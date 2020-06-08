package git.arcanesunku.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private JFrame m_Frame;
    private Canvas m_Canvas;

    private Graphics m_Graphics;
    private BufferedImage m_Image;
    private BufferStrategy m_BufferStrategy;

    private boolean m_Closed = false;

    public Window(GameContainer game) {
        Dimension size = new Dimension((int) (game.getWidth() * game.getScale()), (int) (game.getHeight() * game.getScale()));
        m_Image = new BufferedImage(game.getWidth(), game.getHeight(), BufferedImage.TYPE_INT_RGB);
        m_Canvas = new Canvas();

        m_Canvas.setPreferredSize(size);
        m_Canvas.setMinimumSize(size);
        m_Canvas.setMaximumSize(size);

        m_Frame = new JFrame(game.getTitle());
        m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_Frame.setLayout(new BorderLayout());
        m_Frame.add(m_Canvas, BorderLayout.CENTER);
        m_Frame.pack();
        m_Frame.setResizable(false);
        m_Frame.setLocationRelativeTo(null);
        m_Frame.setVisible(true);

        m_Frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                m_Closed = true;
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if(!m_Closed) m_Closed = true;
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        m_Canvas.createBufferStrategy(3);
        m_BufferStrategy = m_Canvas.getBufferStrategy();
        m_Graphics = m_BufferStrategy.getDrawGraphics();
    }

    public void update() {
        m_Graphics.drawImage(m_Image, 0, 0, m_Canvas.getWidth(), m_Canvas.getHeight(), null);
        m_BufferStrategy.show();
    }

    public JFrame getFrame() { return m_Frame; }

    public Canvas getCanvas() {
        return m_Canvas;
    }

    public BufferedImage getImage() {
        return m_Image;
    }

    public boolean hasClosed() {
        return m_Closed;
    }

}
