package Models;

import ProductModels.Product;
import Models.Cash;
import Models.User;
import Models.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket {

    public enum Estado{
        VACIO, ACTIVO, CERRADO
    }

    private String id;
    private Estado estado;
    private final String clientId;
    private final String cashId;
    private final LocalDateTime creationDate;
    private LocalDateTime closeDate;
    private static final int maxProducts = 100;
    private Map<Integer, TicketItem> items = new HashMap<>();

    public Ticket(){
        //initialize default values
        this.clientId = "";
        this.cashId = "";
        this.creationDate = LocalDateTime.now();
        this.id = generateId();
        this.estado = Estado.VACIO;
        this.items = new HashMap<>();
    }

    public Ticket(String clientId, String cashtId, String id) {
        if (clientId == null || clientId.isBlank()) throw new IllegalArgumentException("Client ID required");
        if (cashtId == null || cashtId.isBlank()) throw new IllegalArgumentException("CashtId required");

        this.clientId = clientId;
        this.cashId = cashtId;
        this.id = (id == null || id.isBlank()) ? generateId() : id;
        this.estado = Estado.VACIO;
        this.creationDate = LocalDateTime.now();
        this.items = new HashMap<>();
    }

    //Generate a unique ticket ID based on assignment rules
    private String generateId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String prefix = creationDate.format(formatter);
        int randomNum = new Random().nextInt(100000);
        return prefix + String.format("%05d", randomNum);
    }

    //Mark ticket as closed
    public void closeTicket(){
        if (estado == Estado.CERRADO) throw new IllegalStateException("Ticket already closed");
        this.estado = Estado.CERRADO;
        this.closeDate = LocalDateTime.now();
        updateIdWithCloseDate();
    }

    //Update ID to include closing date
    private void updateIdWithCloseDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        this.id += closeDate.format(formatter);
    }
    public String getId() {return id;}
    public Estado getEstado() {return estado;}
    public String getClientId() {return clientId;}
    public String getCashId() {return cashId;}
    public LocalDateTime getCreationDate() {return creationDate;}
    public LocalDateTime getCloseDate() {return closeDate;}
    public Map<Integer, TicketItem> getItems() {return Collections.unmodifiableMap(items);}

    public void addProduct(Product product, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Invalid quantity.");
        if (items.size() >= maxProducts) throw new IllegalArgumentException("Ticket has reached the limit.");

        TicketItem existingItem = items.get(product.getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new TicketItem(product, quantity));
        }
    }

    public void removeProduct(int id) {
        items.remove(id);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public double calculateTotalWithDiscount() {
        double total = 0.0;
        Map<Category, Integer> countByCategory = new HashMap<>();

        for (TicketItem item : items.values()) {
            Category category = item.getProduct().getCategory();
            countByCategory.put(category, countByCategory.getOrDefault(category, 0) + item.getQuantity());
        }

        for (TicketItem item : items.values()) {
            Product product = item.getProduct();
            double subtotal = item.getSubtotal();
            double discount = 0.0;

            if (countByCategory.get(product.getCategory()) > 1) {
                discount = product.getCategory().getDiscount();
            }
            total += subtotal * (1 - discount);
        }

        return total;
    }

    public Map<Category, Integer> countProductsByCategory() {
        Map<Category, Integer> countByCategory = new HashMap<>();
        for (TicketItem item : items.values()) {
            Category category = item.getProduct().getCategory();
            countByCategory.put(category, countByCategory.getOrDefault(category, 0) + item.getQuantity());
        }
        return countByCategory;
    }

    public void generateTicket() {
        if (isEmpty()) {
            System.out.println("Empty ticket.");
            return;
        }

        Map<Category, Integer> countByCategory = countProductsByCategory();

        List<TicketItem> sorted = new ArrayList<>(items.values());
        sorted.sort(Comparator.comparing(item -> item.getProduct().getName()));

        double total = 0;
        double totalDiscount = 0;

        for (TicketItem item : sorted) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            double discount = 0.0;
            if (countByCategory.get(product.getCategory()) > 1) {
                discount = product.getCategory().getDiscount();
            }

            for (int i = 0; i < quantity; i++) {
                double price = product.getPrice();
                double discountAmount = price * discount;

                System.out.printf(product + " **discount -" + discountAmount + "\n");

                total += price;
                totalDiscount += discountAmount;
            }
        }

        System.out.println("Total price: " + total);
        System.out.println("Total discount: " + totalDiscount);
        System.out.println("Final Price: " + calculateTotalWithDiscount());
    }
}
