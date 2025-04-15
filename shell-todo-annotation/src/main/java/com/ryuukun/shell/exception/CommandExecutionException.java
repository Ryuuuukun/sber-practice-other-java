package com.ryuukun.shell.exception;

import com.ryuukun.shell.command.ShellCommand;

public class CommandExecutionException extends Exception {
    private final ShellCommand command;

    public CommandExecutionException(String message, ShellCommand command) {
        super(message);

        this.command = command;
    }

    @Override
    public String getMessage() {
        return "[" + command.getName() + "] " + super.getMessage();
    }
}
