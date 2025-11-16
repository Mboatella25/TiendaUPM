package Controllers;

import Models.Category;
import ProductModels.*;
import Repository.ProductRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ProductController {

    private final ProductRepository productRepository;
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Agregar producto básico o personalizado
    public String addProduct(String id, String name, String categoryStr, double price, Integer maxPers, List<String> allowedTexts) {

        try {
            String finalId = (id == null || id.isBlank()) ?
                    productRepository.generateUniqueProductId() : id;

            // Verificar que el ID no existe
            if (productRepository.existsById(finalId)) {
                return "Error: Ya existe un producto con ID " + finalId;
            }

            Category category = Category.valueOf(categoryStr.toUpperCase());
            Product product;

            if (maxPers != null && allowedTexts != null) {
                //Crear CustomProduct
                product = new CustomProduct(finalId, name, category, price, maxPers, allowedTexts);
            } else {
                //Crear RegularProduct
                product = new RegularProduct(finalId, name, category, price);
            }

            productRepository.addProduct(product);
            return "Producto añadido: " + finalId+
                    (maxPers != null ? " (personalizable, max: " + maxPers + ")" : "");

        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String addFoodProduct(String id, String name, double price, String expirationStr, int maxPeople) {
        try {
            //Generar ID único si no se proporciona
            String finalId = (id == null || id.isBlank())
                    ? productRepository.generateUniqueProductId()
                    : id;

            // Verificar que el ID no existe
            if (productRepository.existsById(finalId)) {
                return "Error: Ya existe un producto con ID " + finalId;
            }

            LocalDate expiration = LocalDate.parse(expirationStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //VALIDACIÓN TEMPORAL: 3 días
            if (expiration.isBefore(LocalDate.now().plusDays(3))) {
                return "Error: Las comidas requieren 3 días de planificación mínima";
            }

            FoodProduct food = new FoodProduct(finalId, name, price, expiration, maxPeople);
            productRepository.addProduct(food);
            return "Comida añadida: " + finalId;

        } catch (DateTimeParseException e) {
            return "Error: Formato de fecha inválido. Use yyyy-MM-dd";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }


    //Añadir reunión
    public String addMeetingProduct(String id, String name, double price, String expirationStr, int maxPeople) {
        try {
            //Generar ID único si no se proporciona
            String finalId = (id == null || id.isBlank())
                    ? productRepository.generateUniqueProductId()
                    : id;

            //Verificar que el ID no existe
            if (productRepository.existsById(finalId)) {
                return "Error: Ya existe un producto con ID " + finalId;
            }

            LocalDate expiration = LocalDate.parse(expirationStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //VALIDACIÓN TEMPORAL: 12 horas
            if (expiration.atStartOfDay().isBefore(java.time.LocalDateTime.now().plusHours(12))) {
                return "Error: Las reuniones requieren 12 horas de planificación mínima";
            }

            MeetingProduct meeting = new MeetingProduct(finalId, name, price, expiration, maxPeople);
            productRepository.addProduct(meeting);
            return "Reunión añadida: " + finalId;

        } catch (DateTimeParseException e) {
            return "Error: Formato de fecha inválido. Use yyyy-MM-dd";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String updateProduct(String id, String field, String value) {
        Optional<Product> productOpt = productRepository.getProductById(id);
        if (!productOpt.isPresent()) {
            return "Error: Producto no encontrado";
        }

        Product product = productOpt.get();
        switch (field.toLowerCase()) {
            case "name" -> product.setName(value);
            case "category" -> product.setCategory(Category.valueOf(value.toUpperCase()));
            case "price" -> product.setPrice(Double.parseDouble(value));
            default -> throw new IllegalArgumentException("Invalid field.");
        }
        return "Product update: ok.";
    }

    public void removeProduct(String id) {
        if (productRepository.removeProduct(id)) {
            System.out.println("Product removed: ok.\n");
        }
        else
            System.out.println("Product not found.\n");
    }

    public Optional<Product> getProduct(String id) {
        return productRepository.getProductById(id);
    }

    public void listProducts() {
        List<Product> products = productRepository.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("Product list is empty.");
            return;
        }
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println("prod list: ok.\n");
    }
}
