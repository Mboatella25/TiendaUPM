package Controllers;

import ProductModels.Product;
import Models.Ticket;

public class TicketController {
    private Ticket ticket = new Ticket();

    public void newTicket() {
        ticket.clear();
        System.out.println("New ticket started.");
    }

    public void addProduct(Product product, int quantity) {
        ticket.addProduct(product, quantity);
        showCurrentTicket();
        System.out.println("ticket add: ok.\n");
    }

    public void removeProduct(int id) {
        ticket.removeProduct(id);
        showCurrentTicket();
        System.out.println("ticket remove: ok.\n");
    }

    public void printTicket() {
        System.out.println("--- Ticket ---");
        ticket.generateTicket();
        System.out.println("ticket print: ok");
        System.out.println();
        newTicket();
    }

    public void showCurrentTicket() {
        ticket.generateTicket();
    }
}
