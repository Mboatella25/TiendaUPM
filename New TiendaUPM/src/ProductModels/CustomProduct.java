package ProductModels;

import Models.Category;

import java.util.List;

public class CustomProduct extends Product {
    private final int maxCusomizations;
    private final List<String> allowedTexts;

    public CustomProduct(int id, String name, Category category, double price,
                         int maxCusomizations, List<String> allowedTexts) {
        super(id, name, category, price);
        this.maxCusomizations = maxCusomizations;
        this.allowedTexts = allowedTexts;
    }

    @Override
    public boolean isCustomizable() { return true; }

    public int getMaxCusomizations() { return maxCusomizations; }
    public List<String> getAllowedTexts() { return allowedTexts; }
}
