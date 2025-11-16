package ProductModels;

import Models.Category;
import java.util.Objects;

public abstract class Product {
    protected final int maxNameLength = 100;
    protected final String id;
    protected String name;
    protected Category category;
    protected double price;

    public Product(String id, String name,Category category, double price) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("ID no puede ser nulo o vacío");
        if (name == null || name.isBlank() || name.length() > maxNameLength)
            throw new IllegalArgumentException("Invalid name");
        if (price < 0)
            throw new IllegalArgumentException("Price must be >= 0");

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public Category getCategory(){ return category; }
    public double getPrice() { return price; }

    //Setters
    public void setName(String name) {
        if (name == null || name.isBlank() || name.length() > maxNameLength)
            throw new IllegalArgumentException("Invalid name.");
        this.name = name;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setPrice(double price) {
        if (price < 0)
            throw new IllegalArgumentException("Price must be >= 0.");
        this.price = price;
    }


    @Override
    public String toString() {
        return String.format("{class:Product, id:%s, name:'%s', category:%s, price:%.2f}",
                id, name, category, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
