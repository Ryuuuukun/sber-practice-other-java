package com.ryuukun.shell.command;

import com.ryuukun.shell.exception.CommandExecutionException;

public interface ShellCommand {
    String getName();
    String getHelp();

    void execute(String[] args) throws CommandExecutionException;
}
