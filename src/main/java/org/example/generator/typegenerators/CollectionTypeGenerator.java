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
        return new CollectionTypeGenerator(new Random(), 0, 5);
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
    public Object generate(Class<?> type, int depth, Generator generator) {
        Collection<Object> collection = createCollectionInstance(type);

        if (depth >= 3) {
            return collection;
        }

        Class<?> elementType = getElementType(type);
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

    private Class<?> getElementType(Class<?> collectionType) {
        Type genericSuperclass = collectionType.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                Type elementType = actualTypeArguments[0];
                if (elementType instanceof Class) {
                    return (Class<?>) elementType;
                }
            }
        }

        Type[] genericInterfaces = collectionType.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                if (parameterizedType.getRawType() == Collection.class ||
                    parameterizedType.getRawType() == List.class ||
                    parameterizedType.getRawType() == Set.class) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        Type elementType = actualTypeArguments[0];
                        if (elementType instanceof Class) {
                            return (Class<?>) elementType;
                        }
                    }
                }
            }
        }

        return Object.class;
    }
}

