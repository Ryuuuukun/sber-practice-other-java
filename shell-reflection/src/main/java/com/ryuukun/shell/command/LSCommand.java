package com.ryuukun.shell.command;

import java.io.File;
import java.util.Objects;

public class LSCommand implements ShellCommand {
    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public String getHelp() {
        return "вывод содержимого для текущей директории";
    }

    @Override
    public void execute(String[] args) {
        File main = new File(System.getProperty("user.dir"));
        if (main.exists()) {
            File[] list = main.listFiles();

            if (list == null) {
                System.out.println("Директория пустая");
            } else {
                for (var file : list) {
                    System.out.println((file.isFile() ? "f" : "d") + " - " + file.getName());
                }
            }
        }
    }
}
