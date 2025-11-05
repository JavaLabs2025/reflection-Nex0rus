package org.example.generator.typegenerators;

import org.example.generator.Generator;

import java.util.Random;

public class PrimitiveTypeGenerator implements TypeGenerator {
    private final Random random;

    public static PrimitiveTypeGenerator withDefault() {
        return new PrimitiveTypeGenerator(new Random());
    }

    public PrimitiveTypeGenerator(Random random) {
        this.random = random;
    }

    @Override
    public boolean canGenerate(Class<?> type) {
        return type.isPrimitive();
    }

    @Override
    public Object generate(Class<?> type, int depth, Generator generator) {
        if (type == boolean.class) {
            return random.nextBoolean();
        } else if (type == byte.class) {
            return (byte) random.nextInt(256);
        } else if (type == short.class) {
            return (short) random.nextInt(Short.MAX_VALUE + 1);
        } else if (type == int.class) {
            return random.nextInt();
        } else if (type == long.class) {
            return random.nextLong();
        } else if (type == float.class) {
            return random.nextFloat();
        } else if (type == double.class) {
            return random.nextDouble();
        } else if (type == char.class) {
            return (char) (random.nextInt(26) + 'a');
        }
        throw new IllegalArgumentException("Unsupported primitive type: " + type);
    }
}

