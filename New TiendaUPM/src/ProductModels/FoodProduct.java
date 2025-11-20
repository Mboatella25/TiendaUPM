package ProductModels;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FoodProduct extends TemporalProduct {
    private static final int PLANNING_TIME = 3;
    public FoodProduct(String id, String name, double price, LocalDate date, int maxPeople) {
        super(id, name, price, date, maxPeople);
        if (ChronoUnit.DAYS.between(LocalDate.now(), date) < PLANNING_TIME)
            throw new IllegalArgumentException("Food must be planned at least 3 days in advance");
    }

    @Override
    public double calculatePrice(int quantity, List<String> customizations) {
        if (quantity > maxPeople)
            throw new IllegalArgumentException("Exceeds maximum number of people");
        return quantity * price;
    }

    @Override
    public String toString() {
        return String.format("{class:Food, id:%s, name:'%s', price:%.2f, date of Event:%s, max people allowed:%d}",
                id, name, price, date, maxPeople);
    }
}
