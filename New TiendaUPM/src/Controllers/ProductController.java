package Controllers;

import Models.Category;
import ProductModels.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductController {
    private static final int maxProducts = 200;
    private Map<Integer, Product> products = new HashMap<>();

    public void addProduct(int id, String name, String productCategory, double price) {
        if (products.containsKey(id))
            throw new IllegalArgumentException("A product with this ID already exists.");
        if (products.size() > maxProducts)
            throw new IllegalArgumentException("Reached limit of products.");

        Category category = Category.valueOf(productCategory.toUpperCase());
        Product newProduct = new Product(id, name, category, price);
        products.put(id, newProduct);
        System.out.println(newProduct);
        System.out.println("Product added: ok.\n");
    }

    public void updateProduct(int id, String field, String value) {
        Product product = products.get(id);
        if (product == null) throw new IllegalArgumentException("Product not found.");
        switch (field.toLowerCase()) {
            case "name" -> product.setName(value);
            case "category" -> product.setCategory(Category.valueOf(value.toUpperCase()));
            case "price" -> product.setPrice(Double.parseDouble(value));
            default -> throw new IllegalArgumentException("Invalid field.");
        }
        System.out.println("Product update: ok.\n");
    }

    public void removeProduct(int id) {
        products.remove(id);
        System.out.println("Product remove: ok\n");
    }

    public Product getProduct(int id) {
        return products.get(id);
    }

    public void listProducts() {
        if (products.isEmpty()) {
            System.out.println("Product list empty.");
            return;
        }
        for (Product product : products.values()) System.out.println(product);
        System.out.println("prod list: ok.\n");
    }
}
