package com.miolean.arena;

public class Distributer {

    private Handler handler;

    public Distributer(Handler handler) {
        this.handler = handler;
    }

    public void distribute() {
        if(Math.random() < 0.01) handler.add(new Tank("adam"));
    }
}
