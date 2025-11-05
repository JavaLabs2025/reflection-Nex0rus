package org.example.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.example.generator.typegenerators.ClassTypeGenerator;
import org.example.generator.typegenerators.CollectionTypeGenerator;
import org.example.generator.typegenerators.InterfaceTypeGenerator;
import org.example.generator.typegenerators.PrimitiveBoxedTypeGenerator;
import org.example.generator.typegenerators.StringTypeGenerator;
import org.example.generator.typegenerators.TypeGenerator;

public class Generator {
    private static final int MAX_RECURSION_DEPTH = 5;
    private final List<TypeGenerator> generators;
    private final PrimitiveBoxedTypeGenerator defaultGenerator;

    public Generator(String defaultPackage) {
        PrimitiveBoxedTypeGenerator boxedTypeGenerator = PrimitiveBoxedTypeGenerator.withDefault();

        this.defaultGenerator = boxedTypeGenerator;
        this.generators = List.of(
                boxedTypeGenerator,
                StringTypeGenerator.withDefault(),
                CollectionTypeGenerator.withDefault(),
                new ClassTypeGenerator(),
                new InterfaceTypeGenerator(new ImplementationScanner(defaultPackage))
        );
    }

    public Object generateValueOfType(Class<?> clazz) {
        return generateValueOfType((Type) clazz, 0);
    }

    public Object generateValueOfType(Class<?> clazz, int depth) {
        return generateValueOfType((Type) clazz, depth);
    }

    public Object generateValueOfType(Type type, int depth) {
        Class<?> rawType = getRawType(type);

        if (depth >= MAX_RECURSION_DEPTH) {
            return defaultGenerator.canGenerate(rawType)
                    ? defaultGenerator.generate(rawType, type, depth + 1, this)
                    : null;
        }

        for (TypeGenerator generator : generators) {
            if (generator.canGenerate(rawType)) {
                return generator.generate(rawType, type, depth, this);
            }
        }

        return null;
    }

    private static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return (Class<?>) rawType;
            }
        }
        throw new IllegalArgumentException("Cannot extract Class from type: " + type);
    }
}
