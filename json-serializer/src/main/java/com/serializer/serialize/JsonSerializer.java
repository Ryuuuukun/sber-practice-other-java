package com.serializer.serialize;

import com.serializer.serialize.annotation.JsonField;
import com.serializer.serialize.annotation.JsonSerialization;
import com.serializer.serialize.exception.JsonSerializationException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonSerializer {
    private final boolean beauty;

    private final int offset;

    protected JsonSerializer(boolean beauty, int offset) {
        this.beauty = beauty;
        this.offset = offset;
    }

    public static String serialize(Object object) {
        return serialize(object, false, 0);
    }

    public static String serialize(Object object, boolean beauty, int offset) {
        return new JsonSerializer(beauty, offset).process(object, 0);
    }

    public String process(Object object, int level) {
        return switch (object) {
            case null -> "null";
            case String    var1 -> "\"" + var1 + "\"";
            case Character var2 -> "\"" + var2 + "\"";
            case Number    var2 -> var2.toString();
            case Boolean   var3 -> var3.toString();

            case Object[] var5 -> process(List.of(var5), level);
            case List<?>  var5 -> process(var5, level);
            case Set<?>   var6 -> process(var6, level);

            case Map<?, ?> var7 -> process(var7.entrySet().stream()
                    .map((e) -> new Pair<>(e.getKey(), e.getValue()))
                    .toList(), level);

            default -> tryProcessObject(object, level);
        };
    }

    protected String process(Iterable<?> objects, int level) {
        var it = objects.iterator();
        if (!it.hasNext()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder(beauty ? "[\n" : "[");
        while (it.hasNext()) {
            if (beauty) {
                builder.repeat(' ', offset * (level + 1));
            }
            builder.append(process(it.next(), level + 1));

            if (it.hasNext()) {
                builder.append(beauty ? ",\n" : ", ");
            }
        }
        if (beauty) {
            builder.append("\n").repeat(' ', offset * level);
        }

        return builder.append("]").toString();
    }

    protected String tryProcessObject(Object object, int level) {
        if (!object.getClass().isAnnotationPresent(JsonSerialization.class)) {
            throw new JsonSerializationException("Unsupported serialization type - " + object.getClass());
        }

        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields())
                .filter((field) -> field.isAnnotationPresent(JsonField.class))
                .toList();

        var it = fields.iterator();
        if (!it.hasNext()) {
            return "{}";
        }

        StringBuilder builder = new StringBuilder(beauty ? "{\n" : "{");
        while (it.hasNext()) {
            var field = it.next();
            field.setAccessible(true);

            String name = field.getAnnotation(JsonField.class).name().trim();
            if (name.isEmpty()) {
                name = field.getName();
            }

            try {
                Object value = field.get(object);

                if (checkRecursiveLinks(object, value)) {
                    throw new JsonSerializationException("Recursive links cannot be serialized");
                }
                if (beauty) {
                    builder.repeat(' ', offset * (level + 1));
                }
                builder.append("\"").append(name).append("\"")
                        .append(": ")
                        .append(process(value, level + 1));

                if (it.hasNext()) {
                    builder.append(beauty ? ",\n" : ", ");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (beauty) {
            builder.append("\n").repeat(' ', offset * level);
        }

        return builder.append("}").toString();
    }

    protected boolean checkRecursiveLinks(Object object, Object value) {
        if (object == value) {
            return true;
        } else if (value instanceof Object[] objects) {
            return checkRecursiveLinks(object, List.of(objects));
        } else if (value instanceof Iterable<?> objects) {
            for (var obj : objects) {
                if (checkRecursiveLinks(object, obj)) {
                    return true;
                }
            }
            return false;
        } else if (value instanceof Map<?, ?> objects) {
            for (var entry : objects.entrySet()) {
                if (checkRecursiveLinks(object, entry.getKey()) || checkRecursiveLinks(object, entry.getValue())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @JsonSerialization
    protected record Pair<K, V>(@JsonField K key, @JsonField V value) {

    }
}
