package model.catalog;

import java.util.ArrayList;
import model.discounts.*;
import util.Review;

public class Figurine extends Product{
    private double height;
    private double width;
    private double depth;
    private String material;
    private String franchise;

    public Figurine(String name, String description, double price, String image, ArrayList<Category> categories, int stock, ArrayList<Review> reviews, IDiscount discount, double height, double width, double depth, String material, String franchise) {
        super(name, description, price, image, categories, stock, reviews, discount);
        if (height < 0 || width < 0 || depth < 0) {
            throw new IllegalArgumentException("Argumentos invalidos");
        }

        this.height = height;
        this.width = width;
        this.depth = depth;
        this.material = material;
        this.franchise = franchise;
    }
}
