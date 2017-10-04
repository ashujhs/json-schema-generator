package com.ash.jsonschema.test;


import java.util.List;

/**
 * Created by agupt12 on 10/2/16 for ofrresend
 *
 *
 */
public class AppleUseClass {

    public static <T> void appleChecker(List<T> inventory, AppleFormatter<T> formatter){

        for(T t:inventory){
           String result = formatter.accept(t);
            System.out.println(result);
        }
        return;
    }

}
