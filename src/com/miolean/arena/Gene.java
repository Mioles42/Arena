package com.miolean.arena;

//In other words, a KMEM entry.

import java.lang.reflect.Method;

public class Gene {

    Method meaning;
    String description;
    UByte cost;
    UByte weight;

    public Gene(String meaning, String description, UByte cost, UByte weight) {
        this.description = description;
        this.cost = cost;
        this.weight = weight;

        try {
            Method method = Tank.class.getMethod("_" + meaning, int.class, int.class, int.class);
            this.meaning = method;
        } catch (NoSuchMethodException e) {
            System.out.println("Failed to find method " + meaning);
        }

    }

    @Override
    public String toString() {
        return meaning + ": " + description + " [cst " + cost + ", wgt " + weight + "]";
    }

    public Method getMeaning() {
        return meaning;
    }
}
