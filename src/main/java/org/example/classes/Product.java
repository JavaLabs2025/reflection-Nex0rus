package org.example.classes;

import java.util.List;

import com.google.common.base.MoreObjects;
import org.example.Generatable;

@Generatable
public class Product {
    private String name;
    private double price;
    private List<Product> relatedProducts;

    public Product(String name, double price, List<Product> relatedProducts) {
        this.name = name;
        this.price = price;
        this.relatedProducts = relatedProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<Product> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("price", price)
                .add("relatedProducts", relatedProducts)
                .toString();
    }

}
