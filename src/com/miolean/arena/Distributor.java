package com.miolean.arena;

public class Distributor {

    private Handler handler;

    public Distributor(Handler handler) {
        this.handler = handler;
    }

    public void distribute() {
        if(Math.random() < 0.01) {
            //Poor Adam
            Tank adam = new Tank("adam");
            adam.x = Math.random() * Global.ARENA_SIZE;
            adam.y = Math.random() * Global.ARENA_SIZE;
            adam.r = Math.random() * Global.ARENA_SIZE;
            handler.add(adam);
        }
    }
}
