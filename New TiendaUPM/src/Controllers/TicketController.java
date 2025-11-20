package Controllers;

import Models.Cashier;
import ProductModels.Product;
import Models.Ticket;
import Repository.TicketRepository;

import java.util.Collection;
import java.util.List;

public class TicketController {
    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(String ticketId, String cashierId, String clientId) {
        Ticket ticket = ticketRepository.newTicket(ticketId, cashierId, clientId);
        if (ticket == null) {
            System.out.println("Error: cashier or client not found.");
        } else {
            ticketRepository.getTicket(ticketId);
            System.out.println("ticket new: ok");
        }
    }

    public void addProductToTicket(String ticketId, String cashierId, String productId,
                                   int quantity, List<String> customizations) {
        boolean ok = ticketRepository.addTicketProduct(ticketId, cashierId, productId, quantity, customizations);
        if (ok) {
            ticketRepository.getTicket(ticketId).showTicket();
            System.out.println("ticket add: ok");
        } else {
            System.out.println("ERROR: TicketId, CashierId or product invalid.");
        }
    }

    public void listTickets() {
        Collection<Ticket> tickets = ticketRepository.listTickets();
        System.out.println("Ticket list:");
        tickets.forEach(ticket -> System.out.printf("%s - %s\n", ticket.getId(), ticket.getStatus()));
    }

    public void listTicketsByCashier(String cashierId) {
        Cashier cashier = ticketRepository.getCashier(cashierId);
        if (cashier == null) {
            System.out.println("Cashier not found.");
            return;
        }

        System.out.println("Tickets:");
        for (String ticketId : cashier.getTickets()) {
            Ticket ticket = ticketRepository.getTicket(ticketId);
            if (ticket != null) {
                System.out.printf("%s->%s\n", ticket.getId(), ticket.getStatus());
            }
        }
        System.out.println("cash tickets: ok");
    }

    public void printTicket(String ticketId) {
        Ticket ticket = ticketRepository.getTicket(ticketId);
        if (ticket != null) {
            ticket.printTicket();
            System.out.println("ticket print: ok");
        } else {
            System.out.println("Ticket not found.");
        }
    }

    public void removeProductFromTicket(String ticketId, String cashierId, String productId) {
        boolean removed = ticketRepository.removeProductFromTicket(ticketId, cashierId, productId);
        if (removed) {
            ticketRepository.getTicket(ticketId).showTicket();
            System.out.println("ticket remove: ok");
        } else {
            System.out.println("ERROR: TicketId, CashierId or product invalid.");
        }
    }

    public void removeTicketsFromCashier(String id) {
        Cashier cashier = ticketRepository.getCashier(id);
        if (cashier == null) {
            System.out.println("Cashier not found.");
            return;
        }
        ticketRepository.removeTicketsByCashier(cashier);
    }

    public Product getProductFromTicket(String ticketId, String productId) {
        return ticketRepository.getProductFromTicket(ticketId, productId);
    }

    public Ticket getTicket(String id) {
        return ticketRepository.getTicket(id);
    }
}
