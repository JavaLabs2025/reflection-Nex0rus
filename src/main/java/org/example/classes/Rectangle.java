package org.example.classes;

import com.google.common.base.MoreObjects;
import org.example.Generatable;

@Generatable
public class Rectangle implements Shape {
    private double length;
    private double width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    public double getArea() {
        return length * width;
    }

    @Override
    public double getPerimeter() {
        return 2 * (length + width);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("length", length)
                .add("width", width)
                .toString();
    }
}