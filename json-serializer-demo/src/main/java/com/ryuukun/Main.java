package com.ryuukun;

import com.serializer.serialize.JsonSerializer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var person = new Person("Alice", 30, List.of("Reading", "Hikking"));

        // default serialization
        System.out.println(JsonSerializer.serialize(person));

        // beauty json
        System.out.println(JsonSerializer.serialize(person, true, 4));
    }
}