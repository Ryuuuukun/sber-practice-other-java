package com.ryuukun.shell;

import java.io.IOException;
import java.util.Scanner;

public class Shell {
    private final ShellInvoker invoker;

    private boolean runnable;

    public Shell(String directory) throws IOException {
        this.invoker = new ShellInvoker();
        this.runnable = true;

        ShellCommandLoader.loadAllCommands(ClassLoader.getSystemClassLoader(), directory, this)
                .forEach(this.invoker::register);
    }

    public void launch() {
        Scanner scanner = new Scanner(System.in);

        while (runnable) {
            System.out.print("jshell > ");
            invoker.execute(scanner.nextLine().trim().split(" +"));
        }
    }

    public void dispose() {
        runnable = false;
    }

    public ShellInvoker getInvoker() {
        return invoker;
    }
}
