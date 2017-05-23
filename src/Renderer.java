import java.awt.Graphics;
import java.util.List;

/**
 * Created by commandm on 2/16/17.
 */
public class Renderer {

    private List<Entity> entities;

    public Renderer(List<Entity> entities) {
        this.entities = entities;
    }

    public void render(Graphics g) {
        for(Entity e: entities) {
            e.render(g);
        }
    }
}
