package com.ryuukun;

import com.ryuukun.meta.ClassMeta;
import com.ryuukun.meta.FieldMeta;
import com.ryuukun.meta.MethodMeta;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.reflect.Modifier.*;

public class ClassAnalyzer {

    public static ClassMeta analyze(Class<?> clazz) {
        return new ClassMeta(
                clazz.getName(),
                clazz.isInterface(),
                Arrays.stream(clazz.getDeclaredFields()).map(ClassAnalyzer::analyze).toList(),
                Arrays.stream(clazz.getDeclaredMethods()).map(ClassAnalyzer::analyze).toList(),
                getModifiers(clazz.getModifiers())
        );
    }

    public static MethodMeta analyze(Method method) {
        return new MethodMeta(method.getName(), method.getReturnType(), List.of(method.getParameters()),
                getModifiers(method.getModifiers()));
    }

    public static FieldMeta analyze(Field field) {
        return new FieldMeta(field.getName(), field.getType(), getModifiers(field.getModifiers()));
    }

    private static Set<Modifier> getModifiers(int modifier) {
        HashSet<Modifier> res = new HashSet<>();

        if ((modifier & PUBLIC) != 0)        res.add(Modifier.PUBLIC);
        if ((modifier & PROTECTED) != 0)     res.add(Modifier.PROTECTED);
        if ((modifier & PRIVATE) != 0)       res.add(Modifier.PRIVATE);

        if ((modifier & ABSTRACT) != 0)      res.add(Modifier.ABSTRACT);
        if ((modifier & STATIC) != 0)        res.add(Modifier.STATIC);
        if ((modifier & FINAL) != 0)         res.add(Modifier.FINAL);
        if ((modifier & TRANSIENT) != 0)     res.add(Modifier.TRANSIENT);
        if ((modifier & VOLATILE) != 0)      res.add(Modifier.VOLATILE);
        if ((modifier & SYNCHRONIZED) != 0)  res.add(Modifier.SYNCHRONIZED);
        if ((modifier & NATIVE) != 0)        res.add(Modifier.NATIVE);

        return res;
    }
}
