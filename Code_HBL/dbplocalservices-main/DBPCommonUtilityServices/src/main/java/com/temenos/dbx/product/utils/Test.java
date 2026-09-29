package com.temenos.dbx.product.utils;

public class Test {

    public static void main(String[] args) {
        MethodA();// called from line 26 on my code
    }
    
    private static void MethodA() {
        System.out.println(new Exception().getStackTrace()[1].getLineNumber());
    }
}
