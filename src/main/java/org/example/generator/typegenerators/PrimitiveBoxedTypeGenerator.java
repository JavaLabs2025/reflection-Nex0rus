package org.example.generator.typegenerators;

import org.example.generator.Generator;

import java.lang.reflect.Type;
import java.util.Random;

public class PrimitiveBoxedTypeGenerator implements TypeGenerator {
    private final Random random;

    public static PrimitiveBoxedTypeGenerator withDefault() {
        return new PrimitiveBoxedTypeGenerator(new Random());
    }

    public PrimitiveBoxedTypeGenerator(Random random) {
        this.random = random;
    }

    @Override
    public boolean canGenerate(Class<?> type) {
        return type.isPrimitive() ||
               type == Boolean.class ||
               type == Byte.class ||
               type == Short.class ||
               type == Integer.class ||
               type == Long.class ||
               type == Float.class ||
               type == Double.class ||
               type == Character.class;
    }

    @Override
    public Object generate(Class<?> type, Type genericType, int depth, Generator generator) {
        if (type == boolean.class || type == Boolean.class) {
            return random.nextBoolean();
        } else if (type == byte.class || type == Byte.class) {
            return (byte) random.nextInt(256);
        } else if (type == short.class || type == Short.class) {
            return (short) random.nextInt(Short.MAX_VALUE + 1);
        } else if (type == int.class || type == Integer.class) {
            return random.nextInt();
        } else if (type == long.class || type == Long.class) {
            return random.nextLong();
        } else if (type == float.class || type == Float.class) {
            return random.nextFloat();
        } else if (type == double.class || type == Double.class) {
            return random.nextDouble();
        } else if (type == char.class || type == Character.class) {
            return (char) (random.nextInt(26) + 'a');
        }
        throw new IllegalArgumentException("Unsupported primitive or boxed type: " + type);
    }
}

