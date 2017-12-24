package com.miolean.arena;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {


    public static void main(String[] args) {
        Tank abel = new Tank("abel");

        abel.update();

        assertTrue(abel.wmemAt(0) == 0);


    }
}