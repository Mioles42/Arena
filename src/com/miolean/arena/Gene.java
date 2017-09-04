package com.miolean.arena;

//In other words, a KMEM entry.

import java.lang.reflect.Method;

public class Gene {

    Method meaning;
    String description;
    byte cost;
    byte weight;

    public Gene(String meaning, String description, byte cost, byte weight) {
        this.description = description;
        this.cost = cost;
        this.weight = weight;

        try {
            Method method = Tank.class.getMethod("_" + meaning, byte.class, byte.class, byte.class);
            System.out.println("Found method _" + meaning);
            this.meaning = method;
        } catch (NoSuchMethodException e) {
            //Yeah, there's really no recovery from this.
            System.out.println("Failed to find method _" + meaning);
        }

    }

    @Override
    public String toString() {
        return meaning + ": " + description + " [cst " + cost + ", wgt " + weight + "]";
    }
}
