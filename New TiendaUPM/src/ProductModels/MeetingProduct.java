package ProductModels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MeetingProduct extends TemporalProduct {
    private static final int PLANNING_TIME = 12;
    public MeetingProduct(String id, String name, double price, LocalDate date, int maxPeople) {
        super(id, name, price, date, maxPeople);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventStart = date.atStartOfDay();
        if (ChronoUnit.HOURS.between(now, eventStart) < PLANNING_TIME)
            throw new IllegalArgumentException("Meeting requires at least 12 hours notice");
    }

    @Override
    public double calculatePrice(int quantity, List<String> customizations) {
        if (quantity > maxPeople)
            throw new IllegalArgumentException("Exceeds maximum participants");
        return quantity * price;
    }

    @Override
    public String toString() {
        return String.format("{class:Meeting, id:%s, name:'%s', price:%.2f, date of Event:%s, max people allowed:%d}",
                id, name, price, date, maxPeople);
    }
}
