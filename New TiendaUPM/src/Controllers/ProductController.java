package Controllers;

import ProductModels.FoodProduct;
import ProductModels.MeetingProduct;
import ProductModels.Product;
import Repository.ProductRepository;

import java.util.Collection;

public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product);
        if (product instanceof FoodProduct) {
            System.out.println(product);
            System.out.println("prod addFood: ok");
        } else if (product instanceof MeetingProduct) {
            System.out.println(product);
            System.out.println("prod addMeeting: ok");
        } else {
            System.out.println(product);
            System.out.println("prod add: ok");
        }
    }

    public void removeProduct(String id) {
        Product productRemoved = productRepository.getProduct(id);
        boolean removed = productRepository.removeProduct(id);

        if (removed) {
            System.out.println(productRemoved);
            System.out.println("prod remove: ok");
        }  else {
            System.out.println("Product not found.");
        }
    }

    public void updateProduct(String id, String field, String value) {
        boolean updated = productRepository.updateProduct(id, field, value);
        Product productUpdated = productRepository.getProduct(id);
        if (updated) {
            System.out.println(productUpdated);
            System.out.println("prod update: ok");
        } else {
            System.out.println("ERROR: productId, field or value invalid.");
        }
    }

    public void listProducts() {
        System.out.println("Catalog:");
        productRepository.listProducts();
        System.out.println("prod list: ok");
    }

    public Product getProduct(String id) {
        return productRepository.getProduct(id);
    }
}
