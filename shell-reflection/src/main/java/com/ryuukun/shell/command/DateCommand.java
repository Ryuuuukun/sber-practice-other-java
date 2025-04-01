package com.ryuukun.shell.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCommand implements ShellCommand {
    @Override
    public String getName() {
        return "date";
    }

    @Override
    public String getHelp() {
        return "вывод теĸущей даты";
    }

    @Override
    public void execute(String[] args) {
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
    }
}
