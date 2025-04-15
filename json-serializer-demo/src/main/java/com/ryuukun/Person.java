package com.ryuukun;

import com.serializer.serialize.annotation.JsonField;
import com.serializer.serialize.annotation.JsonSerialization;

import java.util.List;

@JsonSerialization
public class Person {
    @JsonField
    private final String name;

    @JsonField
    private final int age;

    @JsonField
    private final List<String> hobbies;

    public Person(String name, int age, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.hobbies = hobbies;
    }
}
