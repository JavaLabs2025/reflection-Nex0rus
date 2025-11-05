package org.example.generator;

import java.util.List;

import org.example.generator.typegenerators.ClassTypeGenerator;
import org.example.generator.typegenerators.CollectionTypeGenerator;
import org.example.generator.typegenerators.InterfaceTypeGenerator;
import org.example.generator.typegenerators.PrimitiveTypeGenerator;
import org.example.generator.typegenerators.StringTypeGenerator;
import org.example.generator.typegenerators.TypeGenerator;
import org.example.generator.typegenerators.WrapperTypeGenerator;

public class Generator {
    private static final int MAX_RECURSION_DEPTH = 5;
    private final List<TypeGenerator> generators;

    public Generator(String defaultPackage) {
        this.generators = List.of(
                PrimitiveTypeGenerator.withDefault(),
                StringTypeGenerator.withDefault(),
                WrapperTypeGenerator.withDefault(),
                CollectionTypeGenerator.withDefault(),
                new ClassTypeGenerator(),
                new InterfaceTypeGenerator(new ImplementationScanner(defaultPackage))
        );
    }

    public Object generateValueOfType(Class<?> clazz) {
        return generateValueOfType(clazz, 0);
    }

    public Object generateValueOfType(Class<?> clazz, int depth) {
        if (depth >= MAX_RECURSION_DEPTH) {
            return null;
        }

        for (TypeGenerator generator : generators) {
            if (generator.canGenerate(clazz)) {
                return generator.generate(clazz, depth, this);
            }
        }

        return null;
    }
}
