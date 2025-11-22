package ProductModels;

import Models.Category;

import java.time.LocalDate;

public abstract class TemporalProduct extends Product {
    protected LocalDate date;
    protected int maxPeople;

    public TemporalProduct(String id, String name, double price, LocalDate date, int maxPeople) {
        super(id, name, price);

        if (date == null)
            throw new IllegalArgumentException("Date cannot be null");
        if (date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Date cannot be in the past");
        this.date = date;

        if (maxPeople <= 0 || maxPeople > 100)
            throw new IllegalArgumentException("Maximum number of people must be between 0 and 100");
        this.maxPeople = maxPeople;
    }

    public LocalDate getDate() { return date; }
    public int getMaxPeople() { return maxPeople; }
}
