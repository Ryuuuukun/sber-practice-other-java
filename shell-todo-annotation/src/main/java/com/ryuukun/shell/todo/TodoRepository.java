package com.ryuukun.shell.todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TodoRepository {
    private final String url;
    private final String table;

    private final Connection connection;

    public TodoRepository(String repository) throws SQLException {
        String[] args = repository.split("-");

        this.url = args[0];
        this.table = args[1];

        this.connection = DriverManager.getConnection(url, args[2], args[3]);

        init();
    }

    private void init() throws SQLException {
        try (Statement statement = connection.createStatement()){
            statement.execute(String.format("""
                    CREATE TABLE IF NOT EXISTS %1$s (
                        id   INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(50),
                        done BOOLEAN
                    )
                    """, table));
        }
    }

    public int create(String name) throws SQLException {
        return requestUpdate("INSERT INTO " + table + "(name, done) VALUES(?, false)", name);
    }

    public int remove(String name) throws SQLException {
        return requestUpdate("DELETE FROM " + table + " WHERE name = ?", name);
    }

    public int complete(String name) throws SQLException {
        return requestUpdate("UPDATE " + table + " SET done = ? WHERE name = ?", true, name);
    }

    public boolean contains(String name) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM " + table + " WHERE name = ?")) {
            bindingArguments(statement, name);
            return statement.executeQuery().next();
        }
    }

    public List<Todo> load() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT name, done FROM " + table)) {
            ResultSet result = statement.executeQuery();

            List<Todo> tasks = new ArrayList<>();
            while (result.next()) {
                tasks.add(new Todo(result.getString(1), result.getBoolean(2)));
            }

            return tasks;
        }
    }

    public List<Todo> filter(Predicate<Todo> predicate) throws SQLException {
        return load().stream().filter(predicate).toList();
    }

    private int requestUpdate(String request, Object... args) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(request)) {
            bindingArguments(statement, args);

            return statement.executeUpdate();
        }
    }

    private void bindingArguments(PreparedStatement statement, Object... args) throws SQLException {
        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case String  var1 -> statement.setString(i + 1, var1);
                case Integer var2 -> statement.setInt(i + 1, var2);
                case Double  var3 -> statement.setDouble(i + 1, var3);
                case Boolean var4 -> statement.setBoolean(i + 1, var4);

                default -> throw new IllegalStateException("Unexpected value: " + args[i]);
            }
        }
    }
}
