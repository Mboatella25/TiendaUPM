package Models;

import ProductModels.CustomProduct;
import ProductModels.Product;

import java.util.List;

public final class TicketItem {
    private final Product product;
    private final int quantity;
    private final List<String> customizations;

    public TicketItem(Product p, int quantity, List<String> customizations) {
        this.product = p;
        this.quantity = quantity;
        this.customizations = customizations;
    }

    public Product getProduct() { return product; }

    public int getQuantity() { return quantity; }

    public List<String> getCustomizations() { return customizations; }

    @Override
    public String toString() {
        // Si es un producto personalizado y tiene customizaciones
        if (product instanceof CustomProduct && customizations != null && !customizations.isEmpty()) {
            return product + ", personalizationList:" + customizations;
        }
        // Para cualquier otro producto
        return product.toString();
    }
}
