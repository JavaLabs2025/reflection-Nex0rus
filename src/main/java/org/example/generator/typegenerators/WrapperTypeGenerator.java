package org.example.generator.typegenerators;

import org.example.generator.Generator;

import java.util.Random;

public class WrapperTypeGenerator implements TypeGenerator {
    private final Random random;

    public static WrapperTypeGenerator withDefault() {
        return new WrapperTypeGenerator(new Random());
    }

    public WrapperTypeGenerator(Random random) {
        this.random = random;
    }

    @Override
    public boolean canGenerate(Class<?> type) {
        return type == Boolean.class ||
               type == Byte.class ||
               type == Short.class ||
               type == Integer.class ||
               type == Long.class ||
               type == Float.class ||
               type == Double.class ||
               type == Character.class;
    }

    @Override
    public Object generate(Class<?> type, int depth, Generator generator) {
        if (type == Boolean.class) {
            return random.nextBoolean();
        } else if (type == Byte.class) {
            return (byte) random.nextInt(256);
        } else if (type == Short.class) {
            return (short) random.nextInt(Short.MAX_VALUE + 1);
        } else if (type == Integer.class) {
            return random.nextInt();
        } else if (type == Long.class) {
            return random.nextLong();
        } else if (type == Float.class) {
            return random.nextFloat();
        } else if (type == Double.class) {
            return random.nextDouble();
        } else if (type == Character.class) {
            return (char) (random.nextInt(26) + 'a');
        }
        throw new IllegalArgumentException("Unsupported wrapper type: " + type);
    }
}

