package ProductModels;

import Models.Category;

import java.time.LocalDate;

public class TemporalProduct extends Product {
    private final LocalDate expirationDate;
    private final int maxPeople;

    public TemporalProduct(int id, String name, double price,
                           LocalDate expirationDate, int maxPeople) {
        super(id, name, null, price);

        if (maxPeople <= 0 || maxPeople > 100) throw new IllegalArgumentException("maxPeople [1,100]");

        this.expirationDate = expirationDate;
        this.maxPeople = maxPeople;
    }

    public LocalDate getExpirationDate() { return expirationDate; }
    public int getMaxPeople() { return maxPeople; }
}
