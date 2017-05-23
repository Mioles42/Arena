import java.util.ArrayList;
import java.util.List;

/**
 * Created by commandm on 5/20/17.
 */
public class Genome {

    public List<Gene> genes;
    public Tank owner;
    int truthiness = 0;


    public Genome(Tank owner) {
        genes = new ArrayList<Gene>();
        this.owner = owner;
    }
}
