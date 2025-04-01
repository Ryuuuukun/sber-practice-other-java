package com.ryuukun;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println(ClassAnalyzer.analyze(ClassLoader.getSystemClassLoader().loadClass(args[0])));
        } catch (ClassNotFoundException e) {
            System.err.println("A class with the name was not found: " + e.getMessage());
        }
    }
}