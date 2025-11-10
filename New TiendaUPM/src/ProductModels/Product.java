package ProductModels;

import Models.Category;

public class Product {
    private static final int maxNameLength = 100;
    private final int id;
    private String name;
    private Category category;
    private double price;

    public Product(int id, String name, Category category, double price) {
        if (id < 0) throw new IllegalArgumentException("ID must be positive");
        if (name == null || name.isBlank() || name.length() > maxNameLength) throw new IllegalArgumentException("Invalid name");
        if (price < 0) throw new IllegalArgumentException("Price must be > 0");

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public double getPrice() { return price; }

    public void setName(String name) {
        if (name == null || name.isEmpty() || name.length() > maxNameLength)
            throw new IllegalArgumentException("Invalid name.");
        this.name = name;
    }

    public void setCategory(Category category) { this.category = category; }

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Price must be > 0.");
        this.price = price;
    }

    public boolean isCustomizable() { return false; }

    @Override
    public String toString() {
        return "{class:Product, id:" + id +
                ", name:\'" + name +
                "\', category:" + category +
                ", price:" + price + "}";
    }
}
