package Repository;

import Models.Ticket;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;


public class TicketRepository {
    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    //Añadir ticket con verificación de ID único
    public void addTicket(Ticket ticket) {
        if (tickets.containsKey(ticket.getId()))
            throw new IllegalArgumentException("Ticket with id " + ticket.getId() + " already exists");
        tickets.put(ticket.getId(), ticket);
    }
    //Obtener ticket por ID
    public Optional<Ticket> getTicketById(String id) {
        return Optional.ofNullable(tickets.get(id));
    }
    // Eliminar ticket
    public boolean removeTicket(String id) {
        return tickets.remove(id) != null;
    }

    //Listar todos los tickets
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets.values());
    }

    //Obtener tickets por cajero
    public List<Ticket> getTicketsByCashId(String cashId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getCashId().equals(cashId))
                .sorted(Comparator.comparing(Ticket::getId)) // Ordenados por ID
                .collect(Collectors.toList());
    }

    //Obtener tickets por cliente
    public List<Ticket> getTicketsByClientId(String clientId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    //Verificar si ticket existe
    public boolean existsById(String id) {
        return tickets.containsKey(id);
    }

    //Obtener tickets activos de un cajero
    public List<Ticket> getActiveTicketsByCashId(String cashId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getCashId().equals(cashId) &&
                        ticket.getEstado() == Ticket.Estado.ACTIVO)
                .collect(Collectors.toList());
    }

    //Eliminar todos los tickets de un cajero (cuando se elimina el cajero)
    public void removeTicketsByCashId(String cashId) {
        List<String> ticketsToRemove = tickets.values().stream()
                .filter(ticket -> ticket.getCashId().equals(cashId))
                .map(Ticket::getId)
                .collect(Collectors.toList());

        ticketsToRemove.forEach(tickets::remove);
    }

    //Contar tickets totales
    public int getTicketCount() {
        return tickets.size();
    }
}
