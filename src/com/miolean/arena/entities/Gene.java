package com.miolean.arena.entities;

//In other words, a KMEM entry.

import java.lang.reflect.Method;
import java.util.Scanner;

public class Gene {

    private static String version;
    private Method meaning;
    private String description;
    private String category;
    private String arg0Description;
    private String arg1Description;
    private String arg2Description;
    private String notes;
    double cost;
    int weight;
    int bonus;

    public static Gene[] loadAll() {
        Gene[] KMEM = new Gene[256];
        String[][] data = new String[256][];

        Scanner in = new Scanner(Robot.class.getClassLoader().getResourceAsStream("cfg/ergo_origin.csv"));
        in.useDelimiter("\n");

        in.next(); //First line is blank, sorry
        version = in.next().replaceAll(",", "");
        in.next(); //Be rid of the headers


        for(int i = 0; i < 256 && in.hasNext(); i++) {
            data[i] = in.next().split(",");
        }

        int i = 0;

        for (i = 0; i < data.length; i++) {

            KMEM[i] = new Gene();
            KMEM[i].category = (data[i][0].equals("")) ? "None" : data[i][0];
            //column 1 is ID (opcode); that's not useful here
            //column 2 is name (meaning); we'll get that at the end
            KMEM[i].description = data[i][3];
            KMEM[i].weight = (data[i][4].equals("")) ? 0 : Integer.parseInt(data[i][4]);
            KMEM[i].cost = (data[i][5].equals("")) ? 0 : Double.parseDouble(data[i][5]);
            KMEM[i].bonus = (data[i][6].equals("")) ? 0 : Integer.parseInt(data[i][6]);
            KMEM[i].arg0Description = data[i][7];
            KMEM[i].arg1Description = data[i][8];
            KMEM[i].arg2Description = data[i][9];
            KMEM[i].notes = data[i][10];

            try {
                KMEM[i].meaning = Robot.class.getMethod("_" + data[i][2], int.class, int.class, int.class);
            } catch (NoSuchMethodException e) {
                System.err.println("Gene mismatch: " + data[i][2] + " defined in origin file but not substantiated");
            }
            if(KMEM[i].meaning == null) try {
                KMEM[i].meaning = Robot.class.getMethod("_UNDEF", int.class, int.class, int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        for(Method m: Robot.class.getDeclaredMethods()) {
            if(m.getName().charAt(0) == '_') {
                //For all the Robot methods that it looks like we've designated as reflected
                boolean defined = false;
                for(Gene g: KMEM) if(g.meaning == m) defined = true;
                if(! defined) System.err.println("Gene mismatch: " + m.getName() + " present but not defined in origin file");
            }
        }


        return KMEM;
    }

    private Gene() {
    }

    @Override
    public String toString() {
        return (meaning == null? "Unlisted" : meaning.getName().substring(1)) + ": " + description + "     [cost " + cost + ", weight " + weight + "]";
    }

    Method getMeaning() {
        return meaning;
    }
}
