package com.ryuukun.shell.command;

import com.ryuukun.shell.Shell;

public class QuitCommand implements ShellCommand {
    private final Shell shell;

    public QuitCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public String getName() {
        return "quit";
    }

    @Override
    public String getHelp() {
        return "завершение работы приложения";
    }

    @Override
    public void execute(String[] args) {
        shell.dispose();
    }
}
