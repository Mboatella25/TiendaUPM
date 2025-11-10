package ProductModels;

import java.time.LocalDate;

public class FoodProduct extends TemporalProduct {
    public FoodProduct(int id, String name, double pricePerPerson,
                       LocalDate expirationDate, int maxPeople) {
        super(id, name, pricePerPerson, expirationDate, maxPeople);
    }
}
