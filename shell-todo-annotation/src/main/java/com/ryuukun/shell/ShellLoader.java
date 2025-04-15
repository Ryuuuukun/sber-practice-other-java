package com.ryuukun.shell;

import com.ryuukun.shell.command.ShellCommand;
import com.ryuukun.shell.command.ShellCommandHandler;
import com.ryuukun.shell.command.annotation.Commands;
import com.ryuukun.shell.command.annotation.Handle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ShellLoader {
    private final ClassLoader loader;

    public ShellLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public List<ShellCommand> loadAllCommands(String directory, Shell shell) throws IOException {
        List<ShellCommand> commands = new ArrayList<>();
        for (var clazz : loadAllCommandClasses(directory)) {
            Optional<Constructor<?>> constructorVar1 = Arrays.stream(clazz.getConstructors())
                    .filter(c -> c.getParameterCount() == 1 && Shell.class.isAssignableFrom(c.getParameterTypes()[0]))
                    .findAny();
            Optional<Constructor<?>> constructorVar2 = Arrays.stream(clazz.getConstructors())
                    .filter(c -> c.getParameterCount() == 0).findAny();

            Object instance = null;
            try {
                if (constructorVar1.isPresent()) {
                    instance = constructorVar1.get().newInstance(shell);
                } else if (constructorVar2.isPresent()) {
                    instance = constructorVar2.get().newInstance(shell);
                } else {
                    System.err.println("An empty constructor was expected: " + clazz);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            if (instance != null) {
                if (instance instanceof ShellCommand command) {
                    commands.add(command);
                }

                if (clazz.isAnnotationPresent(Commands.class)) {
                    for (var method : Arrays.stream(clazz.getDeclaredMethods()).filter((m) -> m.isAnnotationPresent(Handle.class)).toList()) {
                        method.setAccessible(true);

                        Handle annotation = method.getAnnotation(Handle.class);
                        try {
                            String name = annotation.name().trim();
                            if (name.isEmpty()) {
                                name = method.getName();
                            }

                            String prefix = clazz.getAnnotation(Commands.class).prefix().trim();
                            if (!prefix.isEmpty()) {
                                name = prefix + "-" + name;
                            }

                            var handle = MethodHandles.lookup()
                                    .findVirtual(clazz, method.getName(), MethodType.methodType(void.class, List.of(String[].class)))
                                    .bindTo(instance);

                            commands.add(new ShellCommandHandler(name, annotation.help(), handle));
                        } catch (NoSuchMethodException | IllegalAccessException e) {
                            System.err.println("An error occurred while loading the method or the method signature does not match the ShellCommand interface: " +
                                    clazz.getCanonicalName() + "::" + method.getName());
                        }
                    }
                }
            }
        }

        return commands;
    }

    public List<Class<?>> loadAllCommandClasses(String directory) throws IOException {
        var stream = loader.getResourceAsStream(directory.replace(".", "/"));

        if (stream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                List<Class<?>> classes = new ArrayList<>();
                while (reader.ready()) {
                    String name = reader.readLine();

                    if (name.endsWith(".class")) {
                        try {
                            Class<?> clazz = loader.loadClass(directory + "." + name.split("\\.")[0]);

                            if ((ShellCommand.class.isAssignableFrom(clazz) || clazz.isAnnotationPresent(Commands.class)) &&
                                    !clazz.isInterface() && !ShellCommandHandler.class.isAssignableFrom(clazz)) {
                                classes.add(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            System.err.println("Failed to download or locate the class: " + directory + "." + name);
                        }
                    } else {
                        classes.addAll(loadAllCommandClasses(directory + "." + name));
                    }
                }

                return classes;
            }
        } else {
            throw new FileNotFoundException("There is no directory with that name: " + directory);
        }
    }
}
