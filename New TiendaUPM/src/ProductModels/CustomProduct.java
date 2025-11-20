package ProductModels;

import Models.Category;

import java.util.ArrayList;
import java.util.List;

public class CustomProduct extends BasicProduct {
    private final int maxPersonalizations;
    private final List<String> personalizationList;
    public CustomProduct(String id, String name, Category category, double price, int maxPersonalizations) {
        super(id, name, category, price);
        this.maxPersonalizations = maxPersonalizations;
        this.personalizationList = new ArrayList<>();
    }

    public int getMaxPersonalizations() { return maxPersonalizations; }
    public List<String> getPersonalizationList() { return personalizationList; }

    public void addPersonalizations(List<String> customizations) {
        if (customizations != null) {
            int applied = Math.min(customizations.size(), maxPersonalizations);
            personalizationList.clear();
            personalizationList.addAll(customizations.subList(0, applied));
        }
    }

    @Override
    public double calculatePrice(int quantity, List<String> customizations) {
        double base = super.calculatePrice(quantity, customizations);

        if (customizations != null && !customizations.isEmpty()) {
            // Devuelve el nùmero de customizations si no se llega al màximo o el màximo
            int applied = Math.min(customizations.size(), maxPersonalizations);
            // recargo del 10% por cada texto personalizado
            double surcharge = price * 0.10 * applied;
            base += surcharge * quantity;
        }

        return base;
    }

    @Override
    public double calculateDiscount(int quantity, List<String> customizations, int sameCategoryCount) {
        if (sameCategoryCount <= 2) return 0.0;

        double price = calculatePrice(1, customizations);
        double discount = getCategory().getDiscount();
        return (price * quantity) * discount;
    }


    @Override
    public String toString() {
        return String.format(
                "{class:ProductPersonalized, id:%s, name:'%s', category:%s, price:%.1f, maxPersonal:%d%s}",
                getId(), getName(), getCategory(), getPrice(), maxPersonalizations,
                (personalizationList.isEmpty() ? "" : ", personalizationList:" + personalizationList)
        );
    }
}
