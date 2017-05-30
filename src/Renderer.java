import java.awt.Graphics;

/**
 * Created by commandm on 2/16/17.
 */
public class Renderer {

    private Entity[] entities;

    public Renderer(Entity[] entities) {
        this.entities = entities;
    }

    public void render(Graphics g) {
        for(Entity e: entities) {
            e.render(g);
        }
    }
}
