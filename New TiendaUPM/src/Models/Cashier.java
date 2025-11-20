package Models;
import java.util.ArrayList;
import java.util.List;

public final class Cashier extends User{
    private final List<String> createdTickets;
    public Cashier(String id, String name, String email) {
        super(id, name, email);
        createdTickets = new ArrayList<>();
    }

    public void addTicket(String ticketId) { createdTickets.add(ticketId); }
    public List<String> getTickets() { return createdTickets; }

    @Override
    public String toString() {
        return String.format("Cash{identifier:'%s', name:'%s', email:'%s'}", getId(), getName(), getEmail());
    }
}