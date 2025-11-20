package ProductModels;

import java.util.List;

public interface Discountable {
    double calculateDiscount(int quantity, List<String> customizations, int sameCategoryQuantity);
}
