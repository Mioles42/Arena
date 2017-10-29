package com.miolean.arena;

//In other words, a KMEM entry.

import java.lang.reflect.Method;

public class Gene {

    Method meaning;
    String description;
    UByte cost;
    UByte weight;

    public static final String[] GENE_CATEGORIES = {
            "Reserved",
            "Branching",
            "Memory",
            "Physics",
            "Sight",
            "Action",
            "Math",
            "Recognition",
            "Interaction",
            "State",
            "Stats",
            "Space",
            "Tracking",
            "Expansion",
            "Query",
            "Generation"
    };

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
        return (meaning == null? "Unlisted" : meaning.getName().substring(1)) + ": " + description + "     [cost " + cost + ", weight " + weight + "]";
    }

    public Method getMeaning() {
        return meaning;
    }
}
