import java.util.List;

/**
 * Created by commandm on 2/19/17.
 */
public class Handler {

    private Entity[] entities;

    public Handler(Entity[] entities) {
        this.entities = entities;
    }

    public void update() {
        for(Entity e: entities) {
            e.update(this);
            for(Entity j: entities) {
                if(e.intersectsWith(j) && e != j) e.intersect(j);
            }
        }

    }

    public Entity entityAt(int location) {
        return entities[location];
    }

    public Entity closestToDistance(int x, int y, int distance) {
        int record = 0xFFFF;
        Entity result = null;
        for(Entity e: entities) {
            if(e == null) continue;
            int closeness = ((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y) - distance * distance)
            if(closeness < record) {
                record = closeness;
                result = e;
            }
        }
        return result;
    }

    public Entity closestToValue(int x, int y, int searchRadius, int dataSpace, int value) {
        int record = 0xFFFF;
        Entity result = null;
        for(Entity e: entities) {
            if(e == null || !(e instanceof Tank)) continue;
            if ((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y) > searchRadius * searchRadius) continue;
            int closeness = ((Tank) e).valueOfGene(dataSpace, ((Tank)e)) - value
            if(closeness < record) {
                record = closeness;
                result = e;
            }
        }
        return result;
    }
}
