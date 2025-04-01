package com.ryuukun.shell.command;

public interface ShellCommand {
    String getName();

    String getHelp();

    void execute(String[] args);
}
