package com.miolean.arena;

public class Distributor {

    private Handler handler;

    Distributor(Handler handler) {
        this.handler = handler;
    }

    void distribute() {
        if(Math.random() < 0.05) {
            Cog cog = new Cog(1 + (int) (5 * Math.random()));
            cog.x = Math.random() * Global.ARENA_SIZE;
            cog.y = Math.random() * Global.ARENA_SIZE;
            cog.r = Math.random() * Global.ARENA_SIZE;
            handler.add(cog);
        }


    }

}
