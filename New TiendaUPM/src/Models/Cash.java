package Models;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Cash  extends User{
    private final List<Ticket> tickets = new ArrayList<>();
    //id tiene que ser "UW" + 7 digitos aleatorios / puede pasarse o ser autogenerado
    public Cash(String id, String name, String email) {
        super(validarOGenerarId(id), name, email);
    }
    private static String validarOGenerarId(String id) {
        if (id == null || id.isBlank()) {
            return generarId();
        }
        if (!id.matches("UW\\d{7}")) throw new IllegalArgumentException("El id no es valido, El id del cajero debe ser 'UW' + 7 digitos");
        return id;
    }

    private static String generarId() {
        Random r = new Random();
        int num = r.nextInt(9000000) + 1000000;
        return "UW" + num;
    }

    public boolean canModifyTicket(String ticketId) {
        return tickets.stream().anyMatch(t -> t.getId().equals(ticketId));
    }

    //para gestión de tickets
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }
    public List<Ticket> getTickets() {
        return List.copyOf(tickets);
    }
    @Override
    public String toString() {
        return String.format("{class:Cajero, id:%s, nombre:'%s', email:%s}", getId(), getName(), getEmail());
    }
}
