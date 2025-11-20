package Repository;

import Models.Category;
import ProductModels.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ProductRepository {
    private static final int MAX_PRODUCTS = 200;
    private final Map<String, Product> products = new HashMap<>();
    private final Set<String> usedIds = ConcurrentHashMap.newKeySet(); // No se permiten duplicados

    public void addProduct(Product product) {
        if (products.size() >= MAX_PRODUCTS)
            throw new IllegalArgumentException("Reached the limit of products: " + MAX_PRODUCTS);

        // Si el producto ya tiene ID, verifica que no exista
        if (product.getId() != null && !product.getId().isEmpty()) {
            if (products.containsKey(product.getId())) {
                throw new IllegalArgumentException("Product ID already exists.");
            }
        } else {
            // Genera nuevo ID solo si no tiene
            product.setId(generateProductId());
        }

        // Agrega el ID usado y el producto
        usedIds.add(product.getId());
        products.put(product.getId(), product);
    }

    public boolean updateProduct(String id, String field, String value) {
        Product product = products.get(id);
        if (product == null) return false;

        switch (field.toUpperCase()) {
            case "NAME" -> product.setName(value);
            case "CATEGORY" -> {
                if (product instanceof BasicProduct bp) {
                    bp.setCategory(Category.valueOf(value.toUpperCase()));
                } else return false;
            }
            case "PRICE" -> {
                try {
                    product.setPrice(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            default -> { return false; }
        }
        return true;
    }

    public boolean removeProduct(String id) {
        Product removed = products.remove(id);
        if (removed != null) {
            usedIds.remove(id);
            return true;
        }
        return false;
    }

    public void listProducts() {
        for (Product product : products.values()) {
            System.out.println(product);
        }
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    private String generateProductId() {
        Random random = new Random();
        String id;
        do {
            id = String.format("%05d", random.nextInt(100000));
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }
}
