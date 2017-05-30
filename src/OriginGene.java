import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by commandm on 5/20/17.
 */
public class OriginGene extends Gene {

    private Method method;

    public OriginGene(Genome genome, Method method) {
        super(genome);
        this.method = method;
    }


    @Override
    public byte getSource() {
        return SOURCE_NATIVE;
    }

    @Override
    public int getNumberOfParameters() {
        return 0;
    }

    @Override
    public int actuate(Tank t, Object... args) {
        int result = 0x0000;
        try {
            result = (int) method.invoke(t, args);
        } catch (IllegalAccessException e) {
            System.err.println("Error in accessing method " + method.getName() + ".");
            e.printStackTrace();

        } catch (InvocationTargetException e) {
            System.err.println("Error in invoking method " + method.getName() + " on given Tank");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isProtected() {
        return true;
    }
}
