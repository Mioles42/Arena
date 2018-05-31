package com.miolean.arena.framework;

import java.util.HashMap;
import java.util.Map;

public class Debug {

    private static Map<String, Long> timeLog = new HashMap<>();

    public static void logTime(String category, long value) {
        timeLog.put(category, value);
    }

    public static String getDebugLog() {

        if(timeLog.get("Render") != null && timeLog.get("Update") != null && timeLog.get("Display") != null)
            timeLog.put("Total", timeLog.get("Update") + timeLog.get("Render") + timeLog.get("Display"));
        else timeLog.put("Total", 1L);

        long totalTime = timeLog.get("Total");
        for(long t: timeLog.values()) totalTime += t;

        StringBuilder b = new StringBuilder();

        for(String category: timeLog.keySet()) {
            b.append(category).append(": ").append(timeLog.get(category) / 1000).append("ps [").append(timeLog.get(category) / totalTime * 100).append("%]\n");
        }

        return b.toString();
    }
}
