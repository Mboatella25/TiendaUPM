package Repository;

import Models.Cashier;
import Models.Client;
import Models.Ticket;
import ProductModels.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;


public class TicketRepository {
    private final Map<String, Ticket> tickets = new HashMap<>();
    private final Set<String> ticketUsedIds = ConcurrentHashMap.newKeySet(); // No se permiten duplicados
    private final ProductRepository products;
    private final UserRepository userRepository;

    public TicketRepository(UserRepository userRepository, ProductRepository products) {
        this.products = products;
        this.userRepository = userRepository;
    }

    public Ticket newTicket(String ticketId, String cashierId, String userId) {
        Cashier cashier = userRepository.getCashier(cashierId);
        Client client = userRepository.getClient(userId);

        if (cashier == null || client == null) return null;

        if (ticketId == null || ticketId.isEmpty()) {
            ticketId = generateTicketId();
        }

        Ticket ticket = new Ticket(ticketId, cashierId, userId);
        tickets.put(ticketId, ticket);
        ticketUsedIds.add(ticketId);

        ticket.showTicket();

        cashier.addTicket(ticketId);
        return ticket;
    }

    public boolean addTicketProduct(String ticketId, String cashierId, String productId,
                                    int amount, List<String> customizations) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket == null) return false;
        if (!ticket.getCashierId().equals(cashierId)) return false;

        Product product = products.getProduct(productId);
        if (product == null) return false;

        ticket.addProduct(product, amount, customizations);
        return true;
    }

    public Ticket getTicket(String id) {
        return tickets.get(id);
    }

    public Product getProductFromTicket(String ticketId, String productId) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket == null) return null;
        return ticket.getProduct(productId);
    }

    public boolean removeProductFromTicket(String ticketId, String cashierId, String productId) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket == null) return false;
        if (!ticket.getCashierId().equals(cashierId)) return false;

        return ticket.removeProduct(productId);
    }

    public Collection<Ticket> listTickets() {
        return tickets.values().stream()
                .sorted(Comparator.comparing(Ticket::getId))
                .collect(Collectors.toList());
    }

    public void removeTicketsByCashier(Cashier cashier) {
        for (String ticketId : cashier.getTickets()) {
            tickets.remove(ticketId);
            ticketUsedIds.remove(ticketId);
        }
    }

    public Cashier getCashier(String id) {
        return userRepository.getCashier(id);
    }

    private String generateTicketId() {
        Random random = new Random();
        String id;
        do {
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ofPattern("dd-MM-yy-HH:mm"));
            id = timestamp + "-" + random.nextInt(100000);
        } while (ticketUsedIds.contains(id));

        ticketUsedIds.add(id);
        return id;
    }
}
