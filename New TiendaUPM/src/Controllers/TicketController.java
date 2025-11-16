package Controllers;

import Models.Cash;
import ProductModels.Product;

import Models.Ticket;
import Repository.ProductRepository;
import Repository.TicketRepository;
import Repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class TicketController {

    private final TicketRepository ticketRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public TicketController(TicketRepository ticketRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.ticketRepo = ticketRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    public String createTicket(String ticketId, String cashId, String clientId) {

        // Verificar que cajero y cliente existen
        if (!userRepo.existsCashier(cashId)) {
            return "Error: Cajero no existe";
        }
        if (!userRepo.existsClient(clientId)) {
            return "Error: Cliente no existe";
        }

        Ticket ticket = new Ticket(ticketId, cashId, clientId);
        ticketRepo.addTicket(ticket);

        // El cajero guarda referencia al ticket
        Optional<Cash> cashOpt = userRepo.getCashById(cashId);
        if (cashOpt.isPresent()) {
            cashOpt.get().addTicket(ticket);
        }
        return "New ticket started.";
    }

    public String addProductToTicket(String ticketId, String cashId, String productId, int amount, List<String> customizations) {
        Optional<Ticket> ticketOpt = ticketRepo.getTicketById(ticketId);
        if (ticketOpt == null || !ticketOpt.isPresent()) {
            return "Error: Ticket no existe";
        }

        Optional<Product> productOpt = productRepo.getProductById(productId);
        if (productOpt == null || !productOpt.isPresent()) {
            return "Error: Producto no existe";
        }

        Ticket ticket = ticketOpt.get();
        Product product = productOpt.get();

        // Verificar permisos del cajero
        if (!ticket.canBeModifiedBy(cashId)) {
            return "Error: Solo el cajero creador puede modificar el ticket";
        }

        // Añadir producto al ticket
        ticket.addProduct(product, amount, customizations);
        return "ticket add: ok.";
    }

    public String removeProductFromTicket(String ticketId, String cashId, String productId) {
        Optional<Ticket> ticketOpt = ticketRepo.getTicketById(ticketId);
        if (!ticketOpt.isPresent()) {
            return "Error: Ticket no existe";
        }

        Ticket ticket = ticketOpt.get();

        // Verificar permisos del cajero
        if (!ticket.canBeModifiedBy(cashId)) {
            return "Error: Solo el cajero creador puede modificar el ticket";
        }

        ticket.removeProduct(productId);
        return "ticket remove: ok.";
    }

    public String printTicket(String ticketId, String cashId) {

        Optional<Ticket> ticketOpt = ticketRepo.getTicketById(ticketId);
        if (!ticketOpt.isPresent()) {
            return "Error: Ticket no existe";
        }
        Ticket ticket = ticketOpt.get();
        // Verificar permisos del cajero
        if (!ticket.canBeModifiedBy(cashId)) {
            return "Error: Solo el cajero creador puede imprimir el ticket";
        }

        System.out.println("--- Ticket ---");
        ticket.generateTicket();
        ticket.closeTicket();
        return "ticket print: ok";
    }

    public String showCurrentTicket() {
        List<Ticket> tickets = ticketRepo.getAllTickets();
        if (tickets.isEmpty()) {
            return "No hay tickets";
        }

        // Ordenar por ID de cajero según requisitos
        tickets.sort((t1, t2) -> t1.getCashId().compareTo(t2.getCashId()));

        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : tickets) {
            sb.append(String.format("{id:%s, estado:%s, cajero:%s}",
                            ticket.getId(), ticket.getEstado(), ticket.getCashId()))
                    .append("\n");
        }
        return sb.toString();
    }

}
