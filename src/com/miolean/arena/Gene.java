package com.miolean.arena;

//In other words, a KMEM entry.

import java.lang.reflect.Method;

public class Gene {

    private Method meaning;
    private String description;
    double cost;
    int weight;

    static final String[] GENE_CATEGORIES = {
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

    Gene(String meaning, String description, double cost, int weight) {
        this.description = description;
        this.cost = cost;
        this.weight = weight;

        try {
            this.meaning = Tank.class.getMethod("_" + meaning, int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            System.out.println("Failed to find method " + meaning);
        }

    }

    @Override
    public String toString() {
        return (meaning == null? "Unlisted" : meaning.getName().substring(1)) + ": " + description + "     [cost " + cost + ", weight " + weight + "]";
    }

    Method getMeaning() {
        return meaning;
    }
}
