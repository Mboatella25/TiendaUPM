package ProductModels;

import Models.Category;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class CustomProduct extends Product {
    private final List<String> customTexts;
    private final int maxCustomizations;
    private final List<String> allowedTexts;

    public CustomProduct(String id, String name, Category category, double baseprice,
                         int maxCustomizations, List<String> allowedTexts) {
        super(id, name, category, baseprice);

        if (maxCustomizations <= 0) {
            throw new IllegalArgumentException("Máximo de personalizaciones debe ser > 0");
        }
        if (allowedTexts == null) {
            throw new IllegalArgumentException("Lista de textos permitidos requerida");
        }

        this.maxCustomizations = maxCustomizations;
        this.allowedTexts = allowedTexts;
        this.customTexts = new ArrayList<>();
    }

    // Añadir texto personalizado
    public void addCustomText(String text) {
        if (customTexts.size() >= maxCustomizations) {
            throw new IllegalArgumentException("Límite de personalizaciones alcanzado");
        }
        if (!allowedTexts.contains(text)) {
            throw new IllegalArgumentException("Texto no permitido: " + text);
        }
        customTexts.add(text);
    }

    @Override
    public double getPrice() {
        //10% de recargo por cada texto
        double basePrice = super.getPrice();
        return basePrice * (1 + 0.1 * customTexts.size());
    }

    //Getters
    public List<String> getCustomTexts() { return Collections.unmodifiableList(customTexts); }
    public int getMaxCustomizations() { return maxCustomizations; }
    public List<String> getAllowedTexts() { return Collections.unmodifiableList(allowedTexts); }

    @Override
    public String toString() {
        return String.format("{class:CustomProduct, id:%s, name:'%s', basePrice:%.2f, customTexts:%s, maxCustomizations:%d, finalPrice:%.2f}",
                id, name, super.getPrice(), customTexts, getPrice());
    }

}
