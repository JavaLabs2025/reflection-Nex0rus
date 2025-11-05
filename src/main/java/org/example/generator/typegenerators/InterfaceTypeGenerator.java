package org.example.generator.typegenerators;

import java.lang.reflect.Proxy;
import java.util.List;

import org.example.generator.Generator;
import org.example.generator.ImplementationScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterfaceTypeGenerator implements TypeGenerator {
    private final Logger log = LoggerFactory.getLogger(InterfaceTypeGenerator.class);
    private final ImplementationScanner scanner;

    public InterfaceTypeGenerator(ImplementationScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public boolean canGenerate(Class<?> type) {
        return type.isInterface();
    }

    @Override
    public Object generate(Class<?> type, int depth, Generator generator) {
        List<Class<?>> implementations = scanner.findImplementations(type);

        if (implementations.isEmpty()) {
            log.info("Creating java proxy for interface {}", type.getName());
            return createProxyForInterface(type, generator);
        }

        for (Class<?> implementation : implementations) {
            try {
                return generator.generateValueOfType(implementation, depth);
            } catch (Exception e) {
                log.warn(
                        "Failed to create instance of implementation {}, processing next implementation",
                        implementation.getName()
                );
            }
        }

        throw new RuntimeException("Failed to create instance of interface type " + type.getName());
    }

    private Object createProxyForInterface(Class<?> interfaceClass, Generator generator) {
        return Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                (_, method, _) -> {
                    Class<?> returnType = method.getReturnType();
                    return generator.generateValueOfType(returnType, 0);
                }
        );
    }
}

