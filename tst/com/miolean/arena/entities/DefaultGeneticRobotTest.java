package com.miolean.arena.entities;

import com.miolean.arena.framework.UByte;

import static org.junit.jupiter.api.Assertions.*;

class DefaultGeneticRobotTest {

    public static void testAll() {

        Arena arena = new Arena();
        GeneticRobot robot = new DefaultGeneticRobot(Robot.class.getClassLoader().getResourceAsStream("gen/test1.ergo"), arena);

        robot.update();

        testRobotLoading();
        testRobotUpdate();

        System.out.println("Tests passed");


    }

    public static void testRobotLoading() {

        Arena arena = new Arena();
        GeneticRobot robot = new DefaultGeneticRobot(Robot.class.getClassLoader().getResourceAsStream("gen/test1.ergo"), arena);

        assertTrue(robot.getName().equals("Test1"));

        assertTrue(robot.wmemAt(0).equals(new UByte(1)));
        assertTrue(robot.wmemAt(7).equals(new UByte(8)));
        assertTrue(robot.wmemAt(25).equals(new UByte(42)));
        assertTrue(robot.wmemAt(76).equals(new UByte(10)));

        assertTrue(robot.pmemAt(0,12).equals(new UByte(2)));
        assertTrue(robot.pmemAt(0, 0).equals(new UByte(1)));
        assertTrue(robot.pmemAt(0, 42).equals(new UByte(0)));

    }

    public static void testRobotUpdate() {

        Arena arena = new Arena();
        GeneticRobot robot = new DefaultGeneticRobot(Robot.class.getClassLoader().getResourceAsStream("gen/test1.ergo"), arena);

        //Does the robot use cogs correctly? This sequence of commands should cost exactly 43.05 cogs
        double expectedValue = 100 - 42 - 1 - Robot.DIFFICULTY;
        robot.setCogs(100);
        robot.update();
        assertTrue(robot.getCogs() <= expectedValue+1 && robot.getCogs() >= expectedValue-1);
    }

    public static void main(String[] args) {
        testAll();
    }

}