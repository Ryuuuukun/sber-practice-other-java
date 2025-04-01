package com.ryuukun;

import com.ryuukun.shell.Shell;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Shell(args[0]).launch();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}