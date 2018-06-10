package com.miolean.arena.genetics;

public @interface GeneDescription {

    String description() default "";
    String arg0() default "";
    String arg1() default "";
    String arg2() default "";

}
