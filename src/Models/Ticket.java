package Models;

import java.util.*;

public class Ticket {
    private static final int maxProducts = 100;
    private Map<Integer, TicketItem> items = new HashMap<>();

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
