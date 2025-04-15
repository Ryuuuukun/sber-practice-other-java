package com.ryuukun.shell;

import com.ryuukun.shell.command.ShellCommand;
import com.ryuukun.shell.exception.CommandExecutionException;

import java.util.HashMap;
import java.util.Map;

public class ShellInvoker {
    private final Map<String, ShellCommand> commands;

    public ShellInvoker() {
        this.commands = new HashMap<>();
    }

    public void register(ShellCommand command) {
        if (!commands.containsKey(command.getName())) {
            commands.put(command.getName(), command);
        } else {
            throw new RuntimeException("A command named \"" + command.getName() + "\" already exists");
        }
    }

    public void execute(String[] args) throws CommandExecutionException {
        if (commands.containsKey(args[0])) {
            commands.get(args[0]).execute(args);
        } else {
            throw new RuntimeException("A command named \"" + args[0] + "\" does not exist");
        }
    }

    public Map<String, ShellCommand> getCommands() {
        return commands;
    }
}
