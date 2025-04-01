package com.ryuukun.meta;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

public class ClassMeta {
    private final String name;

    private final boolean isInterface;

    private final List<FieldMeta> fields;
    private final List<MethodMeta> methods;

    private final Set<Modifier> modifiers;

    public ClassMeta(String name, boolean isInterface, List<FieldMeta> fields, List<MethodMeta> methods, Set<Modifier> modifiers) {
        this.name = name;
        this.isInterface = isInterface;
        this.fields = fields;
        this.methods = methods;
        this.modifiers = modifiers;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(modifiers + " " + (isInterface ? "interface" : "class")
                + " " + name + "\n\n");

        builder.append("Fields: \n");
        fields.forEach((field) -> builder.append("   ").append(field).append("\n"));

        builder.append("\nMethods: \n");
        methods.forEach((method) -> builder.append("   ").append(method).append("\n"));

        return builder.toString();
    }
}
