package com.ash.jsonschema.test;

/**
 * Created by agupt12 on 10/2/16 for ofrresend
 *
 *
 */
public class ApplePrettyPrint<T> implements AppleFormatter<Apple> {

    @Override public String accept(Apple apple) {
        String appleChar = apple.weight>150?" Heavy ":" Light ";
        return("A"+appleChar+apple.color+" apple, weighing "+apple.weight);
    }


}
