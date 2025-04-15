package com.ryuukun.shell.command;

import com.ryuukun.shell.exception.CommandExecutionException;

import java.lang.invoke.MethodHandle;

public class ShellCommandHandler implements ShellCommand {
    private final String name;
    private final String help;

    private final MethodHandle handle;

    public ShellCommandHandler(String name, String help, MethodHandle handle) {
        this.name = name;
        this.help = help;
        this.handle = handle;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHelp() {
        return help;
    }

    @Override
    public void execute(String[] args) throws CommandExecutionException {
        try {
            handle.invoke((Object[]) args);
        } catch (Throwable e) {
            throw new CommandExecutionException(e.getMessage(), this);
        }
    }
}
