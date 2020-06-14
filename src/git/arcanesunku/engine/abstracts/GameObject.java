package git.arcanesunku.engine.abstracts;

import git.arcanesunku.engine.GameContainer;
import git.arcanesunku.engine.IGame;
import git.arcanesunku.engine.gfx.Renderer;

import java.util.ArrayList;

public abstract class GameObject {

    protected ArrayList<Component> components = new ArrayList<>();

    protected String tag;

    protected float posX, posY;
    protected int width, height;
    protected int padding, topPadding;

    protected boolean remove = false;

    public abstract void update(GameContainer gc, IGame g, float dt);
    public abstract void render(GameContainer gc, Renderer r);
    public abstract void updateCollision(GameObject other);

    public void updateComponents(GameContainer gc, IGame g, float dt) {
        for(Component component : components) {
            component.update(gc, g, dt);
        }
    }

    public void renderComponents(GameContainer gc, Renderer r) {
        for(Component component : components) {
            component.render(gc, r);
        }
    }

    public void addComponent(Component c) {
        components.add(c);
    }

    public void removeComponent(String tag) {
        components.removeIf(c -> c.getTag().equalsIgnoreCase(tag));
    }

    public Component findComponent(String tag) {
        for(Component c : components) {
            if(c.getTag().equalsIgnoreCase(tag))
                return c;
        }

        return null;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public boolean shouldRemove() {
        return remove;
    }

}
