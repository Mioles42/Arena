/**
 * Created by commandm on 5/19/17.
 */
public abstract class Gene {

    final byte SOURCE_NATIVE = 0;
    final byte SOURCE_BRANCHING = 1;
    final byte SOURCE_EVOLVED = 2;

    private Genome genome;

    public Gene(Genome source) {
        this.genome = source;
    }

    public Genome getGenome() {
        return genome;
    }
    public abstract byte getSource();
    public abstract int getNumberOfParameters();

    //Hard actuation: Actuate gene and fully complete all actions, changing the state of the target Tank.
    public abstract int actuateHard(Tank t, Object... args);

    //Soft actuation: Actuate gene for return value on given tank, but don't change the state of said Tank.
    public abstract int actuateSoft(Tank t, Object... args);
    public abstract boolean isProtected();

}
