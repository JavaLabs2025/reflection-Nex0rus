package org.example.classes;

import com.google.common.base.MoreObjects;
import org.example.Generatable;

@Generatable
public class Triangle implements Shape {
    private double sideA;
    private double sideB;
    private double sideC;

    public Triangle(double sideA, double sideB, double sideC) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    @Override
    public double getArea() {
        double s = (sideA + sideB + sideC) / 2;
        return Math.sqrt(s * (s - sideA) * (s - sideB) * (s - sideC));
    }

    @Override
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sideA", sideA)
                .add("sideB", sideB)
                .add("sideC", sideB)
                .toString();
    }
}