package com.ryuukun.meta;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

public class MethodMeta {
    private final String name;

    private final Class<?> returnType;
    private final List<Parameter> parameters;

    private final Set<Modifier> modifiers;

    public MethodMeta(String name, Class<?> returnType, List<Parameter> parameters, Set<Modifier> modifiers) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.modifiers = modifiers;
    }

    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            params.append(parameters.get(i).getName())
                    .append(": ")
                    .append(parameters.get(i).getType().getCanonicalName());
            if (i + 1 < parameters.size())
                params.append(", ");
        }

        return modifiers + " " + name + "(" + params + "): " + returnType.getCanonicalName();
    }
}
