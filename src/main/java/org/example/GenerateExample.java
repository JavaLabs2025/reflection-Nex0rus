package org.example;


import java.util.List;

import org.example.classes.BinaryTreeNode;
import org.example.classes.Cart;
import org.example.classes.Example;
import org.example.classes.Product;
import org.example.classes.ProductService;
import org.example.classes.Rectangle;
import org.example.classes.Shape;
import org.example.classes.Triangle;
import org.example.generator.Generator;

public class GenerateExample {
    public static void main(String[] args) {
        var gen = new Generator("org.example");
        try {
            System.out.println("Generating Example: " + gen.generateValueOfType(Example.class));

            System.out.println("Generating Triangle: " + gen.generateValueOfType(Triangle.class));
            System.out.println("Generating Rectangle: " + gen.generateValueOfType(Rectangle.class));
            System.out.println("Generating Shape: " + gen.generateValueOfType(Shape.class));

            System.out.println("Generating Product: " + gen.generateValueOfType(Product.class));
            System.out.println("Generating Cart: " + gen.generateValueOfType(Cart.class));

            System.out.println("Generating BinaryTreeNode: " + gen.generateValueOfType(BinaryTreeNode.class));

            ProductService productService = (ProductService) gen.generateValueOfType(ProductService.class);
            System.out.println(
                    "Generating ProductService: " + productService +
                    ". Calling getProduct(): " + productService.getProduct()
            );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}