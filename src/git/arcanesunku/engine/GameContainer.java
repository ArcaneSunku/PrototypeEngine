package git.arcanesunku.engine;

import git.arcanesunku.engine.gfx.Renderer;

public class GameContainer implements Runnable {
    private final IGame m_Game;

    private Input m_Input;
    private Renderer m_Renderer;
    private Window m_Window;

    private Thread m_Thread;

    private String m_Title = "Engine Prototype";

    private float m_Scale = 3f;
    private int m_Width = 320, m_Height = 240;

    private volatile boolean m_Running = false;

    public GameContainer(IGame game) {
        m_Game = game;
    }

    public void start() {
        if(m_Running) return;

        m_Window = new Window(this);
        m_Renderer = new Renderer(this);
        m_Input = new Input(this);

        m_Thread = new Thread(this, "Game_Thread");
        m_Running = true;
        m_Thread.start();
    }

    public void stop() {
        m_Running = false;
    }

    @Override
    public void run() {
        double now;
        double passedTime;

        double delta = 0;
        double frameTime = 0;
        double updateCap = 1.0D / 60.0D;
        double lastTime = System.nanoTime() / 1000000000.0D;

        int frames = 0, fps = 0;
        boolean render = false;

        while (m_Running) {
            if(m_Window.hasClosed())
                stop();

            now = System.nanoTime() / 1000000000.0D;
            passedTime = now - lastTime;
            lastTime = now;

            delta += passedTime;
            frameTime += passedTime;

            while (delta >= updateCap) {
                delta -= updateCap;
                render = true;

                m_Game.update(this, (float) updateCap);
                m_Input.update();

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            if (render) {
                m_Renderer.clear();
                m_Game.render(this, m_Renderer);
                m_Renderer.process();
                m_Window.update();

                frames++;
                render = false;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        dispose();
    }

    private void dispose() {
        try {
            m_Window.getFrame().dispose();
            m_Thread.join(1);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void setTitle(String title) {
        m_Title = title;
    }

    public void setScale(float scale) {
        m_Scale = scale;
    }

    public void setWidth(int width) {
        m_Width = width;
    }

    public void setHeight(int height) {
        m_Height = height;
    }

    public Renderer getRenderer() { return m_Renderer; }

    public Input getInput() {
        return m_Input;
    }

    public Window getWindow() {
        return m_Window;
    }

    public String getTitle() {
        return m_Title;
    }

    public float getScale() {
        return m_Scale;
    }

    public int getWidth() {
        return m_Width;
    }

    public int getHeight() {
        return m_Height;
    }

}
