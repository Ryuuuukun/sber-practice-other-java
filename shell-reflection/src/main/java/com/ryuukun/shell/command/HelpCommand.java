package com.ryuukun.shell.command;

import com.ryuukun.shell.Shell;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.Arrays;
import java.util.Map;

public class HelpCommand implements ShellCommand {
    private final Shell shell;

    public HelpCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "вывод списĸа доступных ĸоманд или справĸи по ĸонĸретной ĸоманде";
    }

    @Override
    public void execute(String[] args) {
        Map<String, ShellCommand> commands = shell.getInvoker().getCommands();

        if (args.length == 1) {
            System.out.println("Список доступных команд:");
            commands.forEach((name, command) -> System.out.println(" - " + name + " - " + command.getHelp()));
        } else if (args.length == 2 && commands.containsKey(args[1])) {
            System.out.println(args[1] + " - " + commands.get(args[1]).getHelp());
        } else {
            System.out.println("Команды с именем \"" + args[1] + "\" не существует");
        }
    }
}
