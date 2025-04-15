package com.ryuukun.shell.command;

import com.ryuukun.shell.Shell;
import com.ryuukun.shell.command.annotation.Commands;
import com.ryuukun.shell.command.annotation.Handle;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Commands
public class SystemCommands {
    private final Shell shell;

    private final PrintStream printer;

    public SystemCommands(Shell shell) {
        this.shell = shell;
        this.printer = shell.getPrinter();
    }

    @Handle(help = "output the current working directory")
    public void pwd(String[] args) {
        printer.println(FileSystems.getDefault().getPath("").toAbsolutePath());
    }

    @Handle(help = "output the contents of the current directory")
    public void ls(String[] args) {
        String directory = FileSystems.getDefault().getPath("").toAbsolutePath() +
                (args.length > 1 ? args[1] : "");

        File main = new File(directory);
        if (main.exists()) {
            File[] list = main.listFiles();

            if (list == null) {
                printer.println("The directory is empty");
            } else {
                for (var file : list) {
                    printer.println((file.isFile() ? "f" : "d") + " - " + file.getName());
                }
            }
        } else {
            printer.println("This directory does not exist");
        }
    }

    @Handle(help = "current date output")
    public void date(String[] args) {
        printer.println(LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
    }

    @Handle(help = "current time output")
    public void time(String[] args) {
        printer.println(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
    }

    @Handle(help = "display information about all available commands")
    public void help(String[] args) {
        Map<String, ShellCommand> commands = shell.getInvoker().getCommands();

        if (args.length == 1) {
            printer.println("List of available commands:\n");
            commands.forEach((name, command) -> printer.println(" - " + name + " - " + command.getHelp()));
        } else if (args.length == 2 && commands.containsKey(args[1])) {
            printer.println(args[1] + " - " + commands.get(args[1]).getHelp());
        } else {
            printer.println("A command named \"" + args[1] + "\" does not exist");
        }
    }

    @Handle(help = "log out of shell")
    public void exit(String[] args) {
        shell.dispose();
    }
}
