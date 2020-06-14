package git.arcanesunku.engine;

import git.arcanesunku.engine.gfx.Renderer;

public interface IGame {

    void init(GameContainer gc);
    void update(GameContainer gc, float dt);
    void render(GameContainer gc, Renderer r);

}
