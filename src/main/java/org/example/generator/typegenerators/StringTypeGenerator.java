package org.example.generator.typegenerators;

import org.example.generator.Generator;

import java.util.Random;

public class StringTypeGenerator implements TypeGenerator {
    private final Random random;
    private final String charSequence;
    private final int minLength;
    private final int maxLength;

    public static StringTypeGenerator withDefault() {
        return new StringTypeGenerator(
                new Random(),
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                5,
                15
        );
    }

    public StringTypeGenerator(
            Random random,
            String charSequence,
            int minLength,
            int maxLength
    ) {
        this.random = random;
        this.charSequence = charSequence;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean canGenerate(Class<?> type) {
        return type == String.class;
    }

    @Override
    public Object generate(Class<?> type, int depth, Generator generator) {
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(charSequence.charAt(random.nextInt(charSequence.length())));
        }
        return sb.toString();
    }
}

