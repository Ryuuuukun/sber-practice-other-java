package com.ryuukun.shell.todo;

public record Todo(String name, boolean done) {
    @Override
    public String toString() {
        return (done ? "[x]" : "[ ]") + " " + name;
    }
}
