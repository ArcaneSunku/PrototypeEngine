package git.arcanesunku.engine;

import java.awt.*;
import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {
    private static final int MAX_KEYS = 256, MAX_BUTTONS = 5;

    private GameContainer m_GameContainer;

    private boolean[] m_Keys, m_LastKeys;
    private boolean[] m_Buttons, m_LastButtons;

    private int m_Scroll;
    private int m_MouseX, m_MouseY;

    public Input(GameContainer gc) {
        m_GameContainer = gc;

        m_Keys = new boolean[MAX_KEYS];
        m_LastKeys = new boolean[MAX_KEYS];

        m_Buttons = new boolean[MAX_BUTTONS];
        m_LastButtons = new boolean[MAX_BUTTONS];

        m_Scroll = 0;
        m_MouseX = 0;
        m_MouseY = 0;

        addToCanvas(m_GameContainer.getWindow().getCanvas());
    }

    private void addToCanvas(Canvas canvas) {
        canvas.setFocusable(true);

        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);
    }

    public void update() {
        m_Scroll = 0;

        for(int i = 0; i < MAX_KEYS; i++) {
            m_LastKeys[i] = m_Keys[i];
        }

        for(int i = 0; i < MAX_BUTTONS; i++) {
            m_LastButtons[i] = m_Buttons[i];
        }
    }

    // Key Accessors

    public boolean isKey(int keyCode) {
        return m_Keys[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        return (!m_Keys[keyCode] && m_LastKeys[keyCode]);
    }

    public boolean isKeyDown(int keyCode) {
        return (m_Keys[keyCode] && !m_LastKeys[keyCode]);
    }

    // Button Accessors

    public boolean isButton(int button) {
        return m_Buttons[button];
    }

    public boolean isButtonUp(int button) {
        return (!m_Buttons[button] && m_LastButtons[button]);
    }

    public boolean isButtonDown(int button) {
        return (m_Buttons[button] && !m_LastButtons[button]);
    }

    // Key Listener
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        m_Keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        m_Keys[e.getKeyCode()] = false;
    }

    // Mouse Listener
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        m_Buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        m_Buttons[e.getButton()] = false;
    }

    // Mouse Wheel Listener
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        m_Scroll = e.getWheelRotation();
    }

    // Mouse Motion Listener
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        m_MouseX = (int) (e.getX() / m_GameContainer.getScale());
        m_MouseY = (int) (e.getY() / m_GameContainer.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        m_MouseX = (int) (e.getX() / m_GameContainer.getScale());
        m_MouseY = (int) (e.getY() / m_GameContainer.getScale());
    }

    // Getters

    public int getScroll() {
        return m_Scroll;
    }

    public int getMouseX() {
        return m_MouseX;
    }

    public int getMouseY() {
        return m_MouseY;
    }

}
