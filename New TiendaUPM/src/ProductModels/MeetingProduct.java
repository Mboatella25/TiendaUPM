package ProductModels;

import java.time.LocalDate;

public class MeetingProduct extends TemporalProduct {
    public MeetingProduct(int id, String name, double pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, pricePerPerson, expirationDate, maxPeople);
    }
}
