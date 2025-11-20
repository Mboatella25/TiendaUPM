package ProductModels;

import Models.Category;

import java.util.*;

public class BasicProduct extends Product implements Discountable {
    protected Category category;

    public BasicProduct(String id, String name, Category category, double price) {
        super(id, name, price);
        this.category = category;
    }

    @Override
    public double calculatePrice(int quantity, List<String> customizations) {
        // Precio sin descuento
        return price * quantity;
    }

    @Override
    public double calculateDiscount(int quantity, List<String> customizations, int sameCategoryQuantity) {
        // Solo hay descuento si hay más de 2 productos de la misma categoría
        if (sameCategoryQuantity <= 2) return 0.0;

        double discount = category.getDiscount();
        return (price * quantity) * discount;
    }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category;}

    @Override
    public String toString() {
        return String.format("{class:Product, id:%s, name:'%s', category:%s, price:%.2f}",
                id, name, category, price);
    }
}
