package Repository;

import ProductModels.Product;
import ProductModels.FoodProduct;
import ProductModels.MeetingProduct;
import ProductModels.CustomProduct;
import ProductModels.RegularProduct;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ProductRepository {
    private static final int MAX_PRODUCTS = 200;
    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final Set<String> usedIds = ConcurrentHashMap.newKeySet();

    //Añadir producto con verificación de ID único
    public void addProduct(Product product) {
        if (products.size() >= MAX_PRODUCTS) {
            throw new IllegalArgumentException("Se alcanzó el límite máximo de productos: " + MAX_PRODUCTS);
        }
        if (products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Producto con ID " + product.getId() + " ya existe");
        }
        products.put(product.getId(), product);
        usedIds.add(product.getId());
    }

    // Eliminar producto
    public boolean removeProduct(String id) {
        if (products.containsKey(id)) {
            products.remove(id);
            usedIds.remove(id);
            return true;
        }
        return false;
    }
    public Optional<Product> getProductById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    // Listar todos los productos
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    //Verificar si ID existe
    public boolean existsById(String id) {
        return products.containsKey(id);
    }

    //Generar ID único para productos (cuando no se proporciona)
    public String generateUniqueProductId() {
        Random random = new Random();
        String id;
        do {
            // Formato: P + 6 dígitos aleatorios
            int num = random.nextInt(900000) + 100000;
            id = "P" + num;
        } while (usedIds.contains(id));
        return id;
    }

    //Filtrar por tipo
    public List<RegularProduct> getAllRegularProducts() {
        List<RegularProduct> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product instanceof RegularProduct) {
                result.add((RegularProduct) product);
            }
        }
        return result;
    }
    public List<FoodProduct> getFoodProducts() {
        List<FoodProduct> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product instanceof FoodProduct) {
                result.add((FoodProduct) product);
            }
        }
        return result;
    }
    public List<MeetingProduct> getAllMeetingProducts() {
        List<MeetingProduct> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product instanceof MeetingProduct) {
                result.add((MeetingProduct) product);
            }
        }
        return result;
    }

    public List<CustomProduct> getAllCustomProducts() {
        List<CustomProduct> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product instanceof CustomProduct) {
                result.add((CustomProduct) product);
            }
        }
        return result;
    }

    //Contador
    public int getProductCount() {
        return products.size();
    }
}
