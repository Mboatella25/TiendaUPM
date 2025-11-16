package Models;

import ProductModels.Product;
import ProductModels.CustomProduct;
import java.util.List;
import java.util.ArrayList;

public class TicketItem {
    private Product product;
    private int quantity;
    private List<String> customTexts; //solo para CustomProduct

    public TicketItem(Product product, int quantity) {
        this(product, quantity, new ArrayList<>());
    }

    public TicketItem(Product product, int quantity, List<String> customTexts) {
        this.product = product;
        this.quantity = quantity;
        this.customTexts = new ArrayList<>(customTexts);
    }

    //Getters y setters
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public List<String> getCustomTexts() { return new ArrayList<>(customTexts); }

    public double getSubtotal() {
        double price = product.getPrice();

        //Aplicar recargo por personalización
        if (product instanceof CustomProduct cp && !customTexts.isEmpty()) {
            price *= (1 + 0.1 * customTexts.size());
        }

        return price * quantity;
    }
}
