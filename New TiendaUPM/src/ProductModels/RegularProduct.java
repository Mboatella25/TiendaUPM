package ProductModels;

import Models.Category;

public class RegularProduct extends Product {
    private Category category;

    public RegularProduct(String id, String name, Category category, double price) {
        super(id, name, price);
        if (category == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        this.category = category;
    }

    @Override
    public boolean hasCategory() {
        return true;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public boolean isTemporal() {
        return false;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("{class:RegularProduct, id:%s, name:'%s', category:%s, price:%.2f}",
                getId(), getName(), category, getPrice());
    }
}
