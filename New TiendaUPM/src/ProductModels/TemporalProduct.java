package ProductModels;

import Models.Category;

import java.time.LocalDate;

public abstract class TemporalProduct extends Product {
    protected LocalDate date;
    protected int maxPeople;

    public TemporalProduct(String id, String name, double price, LocalDate date, int maxPeople) {
        super(id, name, price);
        this.date = date;
        this.maxPeople = maxPeople;
    }

    public LocalDate getDate() { return date; }
    public int getMaxPeople() { return maxPeople; }
}
