package com.ryuukun.shell;

import com.ryuukun.shell.exception.CommandExecutionException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class Shell {
    private final List<String> arguments;

    private final ShellInvoker invoker;

    private final Scanner scanner;
    private final PrintStream printer;

    private boolean runnable;

    public Shell(List<String> arguments) throws IOException {
        this.arguments = arguments;
        this.runnable = true;

        this.printer = System.out;
        this.scanner = new Scanner(System.in);

        this.invoker = new ShellInvoker();

        new ShellLoader(ClassLoader.getSystemClassLoader())
                .loadAllCommands(arguments.getFirst(), this)
                .forEach(invoker::register);
    }

    public void launch() {
        while (runnable) {
            printer.print("jshell > ");
            try {
                invoker.execute(scanner.nextLine().split(" +"));
            } catch (CommandExecutionException e) {
                printer.println(ConsoleColors.RED + "Exception raised: " + e.getMessage() + ConsoleColors.RESET);
            }
        }
    }

    public void dispose() {
        runnable = false;
        scanner.close();
    }

    public List<String> getArguments() {
        return arguments;
    }

    public ShellInvoker getInvoker() {
        return invoker;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public PrintStream getPrinter() {
        return printer;
    }
}
