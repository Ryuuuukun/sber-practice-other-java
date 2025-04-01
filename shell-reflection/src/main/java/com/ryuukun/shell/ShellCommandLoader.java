package com.ryuukun.shell;

import com.ryuukun.shell.command.ShellCommand;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ShellCommandLoader {
    public static List<ShellCommand> loadAllCommands(ClassLoader loader, String directory, Shell shell) throws IOException {
        List<ShellCommand> commands = new ArrayList<>();
        for (var clazz : loadAllCommandClasses(loader, directory)) {
            Optional<Constructor<?>> constructorVar1 = Arrays.stream(clazz.getConstructors())
                    .filter(c -> c.getParameterCount() == 1 && Shell.class.isAssignableFrom(c.getParameterTypes()[0]))
                    .findAny();
            Optional<Constructor<?>> constructorVar2 = Arrays.stream(clazz.getConstructors())
                    .filter(c -> c.getParameterCount() == 0).findAny();

            try {
                if (constructorVar1.isPresent()) {
                    commands.add((ShellCommand) constructorVar1.get().newInstance(shell));
                } else if (constructorVar2.isPresent()) {
                    commands.add((ShellCommand) constructorVar2.get().newInstance());
                } else {
                    System.err.println("An empty constructor was expected: " + clazz);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return commands;
    }


    public static List<Class<?>> loadAllCommandClasses(ClassLoader loader, String directory) throws IOException {
        var stream = loader.getResourceAsStream(directory.replace(".", "/"));

        if (stream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                List<Class<?>> classes = new ArrayList<>();
                while (reader.ready()) {
                    String name = reader.readLine();

                    if (name.endsWith(".class")) {
                        try {
                            Class<?> clazz = loader.loadClass(directory + "." + name.split("\\.")[0]);

                            if (ShellCommand.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                                classes.add(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            System.err.println("Не удалось загрузить или найти класс: " + directory + "." + name);
                        }
                    } else {
                        classes.addAll(loadAllCommandClasses(loader, directory + "." + name));
                    }
                }

                return classes;
            } catch (IOException e) {
                throw new IOException(e);
            }
        } else {
            throw new FileNotFoundException("There is no directory with that name: " + directory);
        }
    }
}
