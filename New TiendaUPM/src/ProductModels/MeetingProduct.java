package ProductModels;

import Models.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MeetingProduct extends Product {
    private final LocalDate expirationDate;
    private final int maxPeople;

    public MeetingProduct(String id, String name, double pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, null, pricePerPerson);
        if (expirationDate == null) {
            throw new IllegalArgumentException("Fecha requerida");
        }
        if (maxPeople <= 0 || maxPeople > 100) {
            throw new IllegalArgumentException("Participantes [1,100]");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.meetingDateTime = LocalDateTime.parse(dateTimeStr + "T00:00:00");

        LocalDateTime now = LocalDateTime.now();
        if (meetingDateTime.isBefore(now.plusHours(12))) {
            throw new IllegalArgumentException("Las reuniones requieren mínimo 12 horas de planificación.");
        }
        this.expirationDate = expirationDate;
        this.maxPeople = maxPeople;
    }


    public LocalDate getExpirationDate() {
        return getExpirationDate();
    }
    public int getMaxPeople() {
        return maxPeople;
    }
    public String toString() {
        return String.format("{class:MeetingProduct, id:%s, name:'%s', pricePerPerson:%.2f, expiration:%s, maxPeople:%d}",
                id, name, price, expirationDate, maxPeople);
    }
}
