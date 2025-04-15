package com.ryuukun;

import com.ryuukun.shell.Shell;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            new Shell(List.of(args)).launch();
        } catch (IOException e) {
            System.err.println("Shell application could not be started, specified directory does not exist");
        }
    }
}