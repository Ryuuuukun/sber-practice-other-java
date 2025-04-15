package com.ryuukun.shell.todo;

import com.ryuukun.shell.ConsoleColors;
import com.ryuukun.shell.Shell;
import com.ryuukun.shell.command.annotation.Commands;
import com.ryuukun.shell.command.annotation.Handle;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;

@Commands(prefix = "todo")
public class TodoCommands {
    private TodoRepository repository;

    private final PrintStream printer;

    public TodoCommands(Shell shell) {
        this.printer = shell.getPrinter();

        try {
            repository = new TodoRepository(shell.getArguments().get(1));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Handle(help = "reconnect to the repository base")
    public void reconnect(String[] args) {
        if (args.length > 1) {
            try {
                repository = new TodoRepository(args[1]);
                printer.println(todo() + "The connection has been updated");
            } catch (SQLException e) {
                printer.println(ConsoleColors.RED + "Failed to reconnect to the database: " + e.getMessage() + ConsoleColors.RESET);
            }
        } else {
            printer.println("The expected name of the repository was");
        }
    }

    @Handle(help = "create a new todo task")
    public void add(String[] args) {
        if (repository == null) {
            printer.println(todo() + "You are not connected to the repository");
        } else if (args.length > 1) {
            String name = buildName(args);

            try {
                if (repository.contains(name)) {
                    printer.println(todo() + "A task by that name already exists");
                } else {
                    repository.create(name);
                    printer.println(todo() + "The task was successfully created");
                }
            } catch (SQLException e) {
                printer.println(todo() + ConsoleColors.RED + "An error occurred while creating a task: " +
                        e.getMessage() + ConsoleColors.RESET);
            }
        } else {
            printer.println(todo() + "The task name cannot be empty");
        }
    }

    @Handle(help = "remove todo task from the list")
    public void remove(String[] args) {
        if (repository == null) {
            printer.println(todo() + "You are not connected to the repository");
        } else if (args.length > 1) {
            String name = buildName(args);

            try {
                printer.println(todo() + (repository.remove(name) == 1 ? "There is no such task" : "The task has been deleted"));
            } catch (SQLException e) {
                printer.println(todo() + ConsoleColors.RED + "An error occurred while deleting a task: " +
                        e.getMessage() + ConsoleColors.RESET);
            }
        } else {
            printer.println(todo() + "The task name cannot be empty");
        }
    }

    @Handle(help = "complete the task")
    public void complete(String[] args) {
        if (repository == null) {
            printer.println(todo() + "You are not connected to the repository");
        } else if (args.length > 1) {
            String name = buildName(args);

            try {
                if (repository.contains(name)) {
                    repository.complete(name);
                    printer.println(todo() + "The task was accomplished!");
                } else {
                    printer.println(todo() + "There is no such task");
                }
            } catch (SQLException e) {
                printer.println(todo() + ConsoleColors.RED + "An error occurred while performing the taskЖ " +
                        e.getMessage() + ConsoleColors.RESET);
            }
        } else {
            printer.println(todo() + "The task name cannot be empty");
        }
    }

    private String buildName(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            builder.append(args[i]).append(" ");
        }

        return builder.toString();
    }

    @Handle(help = "show all current tasks")
    public void show(String[] args) {
        try {
            List<Todo> tasks = repository.load();

            if (tasks.isEmpty()) {
                printer.println(todo() + "The task list is empty");
            } else {
                printer.println(todo() + "All tasks:\n");
                for (var todo : tasks) {
                    printer.println(" - " + (todo.done() ? ConsoleColors.GREEN : ConsoleColors.RESET) + todo + ConsoleColors.RESET);
                }
            }
        } catch (SQLException e) {
            printer.println(todo() + ConsoleColors.RED + "An error occurred while performing the taskЖ " +
                    e.getMessage() + ConsoleColors.RESET);
        }
    }

    private String todo() {
        return ConsoleColors.YELLOW_BOLD + "TODO: " + ConsoleColors.RESET;
    }
}
