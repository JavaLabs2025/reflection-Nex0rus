package org.example.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.example.Generatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImplementationScanner {
    private static final Logger log = LoggerFactory.getLogger(ImplementationScanner.class);
    private final List<Class<?>> cache;
    private final ClassLoader classLoader;

    public ImplementationScanner(String defaultPackage) {
        this.classLoader = ImplementationScanner.class.getClassLoader();
        cache = scanPackage(defaultPackage)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(Generatable.class))
                .toList();
    }

    public List<Class<?>> findImplementations(Class<?> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            return new ArrayList<>();
        }

        return cache.stream()
                .filter(interfaceClass::isAssignableFrom)
                .filter(clazz ->
                        !Modifier.isAbstract(clazz.getModifiers())
                        && !clazz.isInterface()
                )
                .toList();
    }

    private List<Class<?>> scanPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');

        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();

                if ("file".equals(protocol)) {
                    classes.addAll(scanDirectory(resource, packageName));
                }
            }
        } catch (IOException e) {
            log.warn("Failed to scan package named {}", packageName, e);
        }

        return classes;
    }

    private List<Class<?>> scanDirectory(URL directoryUrl, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            String path = URLDecoder.decode(directoryUrl.getFile(), StandardCharsets.UTF_8);
            File directory = new File(path);
            return scanDirectoryRecursive(directory, packageName);
        } catch (Exception e) {
            return classes;
        }
    }

    private List<Class<?>> scanDirectoryRecursive(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();

        if (!directory.exists() || !directory.isDirectory()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackageName = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                classes.addAll(scanDirectoryRecursive(file, subPackageName));
            } else if (file.getName().endsWith(".class")) {
                String fileName = file.getName().substring(0, file.getName().length() - ".class".length());
                String className = packageName.isEmpty() ? fileName : packageName + "." + fileName;
                Class<?> clazz = loadClass(className);
                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }

        return classes;
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
            return null;
        }
    }
}
