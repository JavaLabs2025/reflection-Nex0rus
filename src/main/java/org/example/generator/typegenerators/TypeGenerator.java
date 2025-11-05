package org.example.generator.typegenerators;

import java.lang.reflect.Type;

import org.example.generator.Generator;

public interface TypeGenerator {
    boolean canGenerate(Class<?> type);
    Object generate(Class<?> type, Type genericType, int depth, Generator generator);
}

