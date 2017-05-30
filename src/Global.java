/**
 * Created by commandm on 5/21/17.
 */


public class Global {

    //Number of ticks in-simulation
    private int time;

    //1 pixel= 1 tank-meter
    static int ARENA_SIZE_PIXELS = 43640;

    public int getTime() {
        return time;
    }

    public void passTime() {
        time++;
    }


    //Testing a change to Global.
    public static int gitTest() {
        return 1;
    }
}
