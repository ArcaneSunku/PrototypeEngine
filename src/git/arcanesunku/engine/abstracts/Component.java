package git.arcanesunku.engine.abstracts;

import git.arcanesunku.engine.GameContainer;
import git.arcanesunku.engine.IGame;
import git.arcanesunku.engine.gfx.Renderer;

public abstract class Component {

    protected GameObject parent;
    protected String tag;

    public abstract void update(GameContainer gc, IGame g, float dt);
    public abstract void render(GameContainer gc, Renderer r);

    public GameObject getParent() {
        return parent;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
