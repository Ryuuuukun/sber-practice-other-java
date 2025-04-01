package com.ryuukun.shell.command;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements ShellCommand {
    @Override
    public String getName() {
        return "time";
    }

    @Override
    public String getHelp() {
        return "вывод теĸущего времени";
    }

    @Override
    public void execute(String[] args) {
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
    }
}
