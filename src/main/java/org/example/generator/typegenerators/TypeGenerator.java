package org.example.generator.typegenerators;

import org.example.generator.Generator;

public interface TypeGenerator {
    boolean canGenerate(Class<?> type);
    Object generate(Class<?> type, int depth, Generator generator);
}

