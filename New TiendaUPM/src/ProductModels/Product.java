package ProductModels;

import java.util.*;

public abstract class Product {
    protected String id;
    protected String name;
    protected double price;

    public Product(String id, String nombre, double precio) {
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre inválido");
        if (precio <= 0) throw new IllegalArgumentException("Precio inválido");
        this.id = id;
        this.name = nombre;
        this.price = precio;
    }

    // Getters y setters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }

    public void setId(String id) { this.id = id; }

    public abstract double calculatePrice(int quantity, List<String> customizations);

    @Override
    public String toString() {
        return String.format("{class:Product, id:%s, name:'%s', price:%.2f}",
                id, name, price);
    }
}
