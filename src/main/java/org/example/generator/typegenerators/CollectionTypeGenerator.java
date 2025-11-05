package org.example.generator.typegenerators;

import org.example.generator.Generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class CollectionTypeGenerator implements TypeGenerator {
    private final Random random;
    private final int minSize;
    private final int maxSize;

    public static CollectionTypeGenerator withDefault() {
        return new CollectionTypeGenerator(new Random(), 1, 5);
    }

    public CollectionTypeGenerator(Random random, int minSize, int maxSize) {
        this.random = random;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public boolean canGenerate(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    @Override
    public Object generate(Class<?> type, Type genericType, int depth, Generator generator) {
        Collection<Object> collection = createCollectionInstance(type);

        Type elementType = getElementType(genericType);
        int size = random.nextInt(maxSize - minSize + 1) + minSize;

        for (int i = 0; i < size; i++) {
            Object element = generator.generateValueOfType(elementType, depth + 1);
            collection.add(element);
        }
        
        return collection;
    }

    private Collection<Object> createCollectionInstance(Class<?> collectionType) {
        if (collectionType == List.class || collectionType.isAssignableFrom(List.class)) {
            return new ArrayList<>();
        } else if (collectionType == Set.class || collectionType.isAssignableFrom(Set.class)) {
            return new HashSet<>();
        } else if (collectionType == Queue.class || collectionType.isAssignableFrom(Queue.class)) {
            return new LinkedList<>();
        }

        return new ArrayList<>();
    }

    private Type getElementType(Type genericType) {
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                return actualTypeArguments[0];
            }
        }

        return Object.class;
    }
}

