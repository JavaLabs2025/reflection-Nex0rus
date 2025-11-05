package org.example.generator.typegenerators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;

import org.example.Generatable;
import org.example.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassTypeGenerator implements TypeGenerator {
    private final Logger logger = LoggerFactory.getLogger(ClassTypeGenerator.class);

    @Override
    public boolean canGenerate(Class<?> type) {
        return !type.isInterface() &&
               !type.isPrimitive() &&
               type != String.class &&
               type.isAnnotationPresent(Generatable.class);
    }

    @Override
    public Object generate(Class<?> type, int depth, Generator generator) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();

        if (constructors.length == 0) {
            throw new IllegalStateException("No constructors found for class: " + type.getName());
        }

        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));
        for (Constructor<?> constructor : constructors) {
            try {
                return tryGenerateForConstructor(constructor, generator, depth);
            } catch (RuntimeException e) {
                logger.warn(
                        "Failed to create instance for type {}, processing next constructor",
                        type.getName(),
                        e
                );
            }
        }

        throw new RuntimeException("Failed to create instance for type " + type.getName());
    }

    private Object tryGenerateForConstructor(Constructor<?> constructor, Generator generator, int depth) {
        constructor.setAccessible(true);

        try {
            Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] initArgs = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> paramType = extractClassFromType(genericParameterTypes[i], parameterTypes[i]);
                initArgs[i] = generator.generateValueOfType(paramType, depth + 1);
            }

            return constructor.newInstance(initArgs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Constructor type instantiation failed for " + constructor.getName(), e);
        }
    }

    private Class<?> extractClassFromType(Type genericType, Class<?> fallbackType) {
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                return (Class<?>) rawType;
            }
        } else if (genericType instanceof Class) {
            return (Class<?>) genericType;
        }
        return fallbackType;
    }
}

