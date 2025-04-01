package com.ryuukun.shell;

import com.ryuukun.shell.command.ShellCommand;

import java.util.Arrays;
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
            throw new IllegalArgumentException("A command with that name already exists: " + command.getName());
        }
    }

    public void execute(String[] args) {
        if (commands.containsKey(args[0])) {
            commands.get(args[0]).execute(args);
        } else {
            System.out.println("Команды с именем \"" + args[0] + "\" не существует");
        }
    }

    public Map<String, ShellCommand> getCommands() {
        return commands;
    }
}
