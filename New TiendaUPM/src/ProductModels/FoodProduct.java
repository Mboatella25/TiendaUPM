package ProductModels;

import java.time.LocalDate;

public class FoodProduct extends Product {
    private final LocalDate expirationDate;
    private final int maxPeople;

    public FoodProduct(String id, String name, double pricePerPerson,
                       LocalDate expirationDate, int maxPeople) {
        super(id, name, null, pricePerPerson); //sin categorías

        if (expirationDate == null) {
            throw new IllegalArgumentException("Fecha de caducidad requerida");
        }
        if (maxPeople <= 0 || maxPeople > 100) {
            throw new IllegalArgumentException("Participantes [1, 100]");
        }
        if (expirationDate.isBefore(LocalDate.now().plusDays(3))) {
            throw new IllegalArgumentException("Las comidas deben planificarse con mínimo 3 días de antelación.");
        }

        this.expirationDate = expirationDate;
        this.maxPeople = maxPeople;
    }


    //Getters
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public int getMaxPeople() {
        return maxPeople;
    }

    @Override
    public String toString() {
        return String.format("{class:FoodProduct, id:%s, name:'%s', pricePerPerson:%.2f, expiration:%s, maxPeople:%d}",
                id, name, price, expirationDate, maxPeople);
    }
}
