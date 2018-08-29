package com.sams.test;

public class ConsoleLogger {
    public static int print (String LOG_TAG, String message) {
        System.out.println(LOG_TAG + ": " + message);
        return 0;
    }
    public static int print (String LOG_TAG, String message, Throwable e) {
        System.out.println(LOG_TAG + ": " + message + " " + e.getMessage());
        return 0;
    }
}