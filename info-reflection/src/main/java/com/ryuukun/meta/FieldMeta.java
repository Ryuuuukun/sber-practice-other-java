package com.ryuukun.meta;

import javax.lang.model.element.Modifier;
import java.util.Set;

public class FieldMeta {
    private final String name;

    private final Class<?> type;

    private final Set<Modifier> modifiers;

    public FieldMeta(String name, Class<?> type, Set<Modifier> modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    @Override
    public String toString() {
        return modifiers + " " + name + ": " + type.getCanonicalName();
    }
}
