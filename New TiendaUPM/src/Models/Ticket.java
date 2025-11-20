package Models;

import ProductModels.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Ticket {
    public enum Status { EMPTY, OPEN, CLOSE }

    private static final int maxProducts = 100;
    private String id;
    private Status status;
    private final List<TicketItem> items;
    private final String cashierId;
    private final String userId;
    private int currentProductTotal = 0;

    public Ticket(String id, String cashierId, String userId) {
        this.id = id;
        this.cashierId = cashierId;
        this.userId = userId;
        this.status = Status.EMPTY;
        this.items = new ArrayList<>();
    }

    public void addProduct(Product product, int quantity, List<String> customizations) {
        if (status == Status.EMPTY) {
            status = Status.OPEN;
        }
        if (status == Status.CLOSE) {
            throw new IllegalStateException("Ticket closed");
        }
        if (product instanceof TemporalProduct) {
            // no permitir duplicar eventos
            for (TicketItem item : items) {
                if (item.getProduct().getId().equals(product.getId())) {
                    throw new IllegalArgumentException("Cannot add the same event twice");
                }
            }
        }

        if (currentProductTotal + quantity > maxProducts) {
            System.out.println("Reached max products.");
            return;
        }

        items.add(new TicketItem(product, quantity, customizations));
        currentProductTotal += quantity;
    }

   public boolean removeProduct(String productId) {
       if (status != Status.OPEN) {
           throw new IllegalStateException("Ticket not active");
       }

       Iterator<TicketItem> iterator = items.iterator();
       boolean removed = false;

       while (iterator.hasNext()) {
           TicketItem item = iterator.next();
           if (item.getProduct().getId().equals(productId)) {
               iterator.remove();
               currentProductTotal -= item.getQuantity();
               removed = true;
               break; // elimina solo la primera coincidencia
           }
       }

       if (items.isEmpty() && status != Status.CLOSE) {
           status = Status.EMPTY;
       }

       return removed;
   }

    public void printTicket() {
        showTicket();
        closeTicket();
    }

    public void showTicket() {
        System.out.println("Ticket : " + id);

        double totalPrice = 0.0;
        double totalDiscount = 0.0;

        // Contar productos por categoría
        Map<Category, Integer> categoryCount = calculateCategoryCounts();

        // Ordenar los items por nombre de producto
        List<TicketItem> sortTicketList = sortItemsByName();

        // Calcular precios y descuentos
        for (TicketItem item : sortTicketList) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            List<String> customizations = item.getCustomizations();

            // Calcular precio con recargos si es CustomProduct
            double productRealPrice = product.calculatePrice(1, customizations);
            double productBasePrice = productRealPrice * quantity;
            totalPrice += productBasePrice;

            double categoryProductsDiscount = 0.0;
            if (product instanceof Discountable discountable) {
                int sameCategoryCount = 0;
                if (product instanceof BasicProduct basicProduct) {
                    sameCategoryCount = categoryCount.getOrDefault(basicProduct.getCategory(), 0);
                }
                categoryProductsDiscount = discountable.calculateDiscount(quantity, customizations, sameCategoryCount);
                totalDiscount += categoryProductsDiscount;
            }

            double discountPerUnit = (quantity > 0) ? categoryProductsDiscount / quantity : 0.0;

            // Mostrar cada prodcuto individualmente
            if (product instanceof TemporalProduct) {
                System.out.printf("  %s, actual people in event:%d\n", product, quantity);
            } else {
                for (int unitIndex = 0; unitIndex < quantity; unitIndex++) {
                    String productDisplay = product.toString();

                    // Si es CustomProduct con personalizaciones, mostrar precio real
                    if (product instanceof CustomProduct && customizations != null && !customizations.isEmpty()) {
                        CustomProduct customProduct = (CustomProduct) product;
                        productDisplay = String.format(
                                "{class:ProductPersonalized, id:%s, name:'%s', category:%s, price:%.1f, maxPersonal:%d, personalizationList:%s}",
                                customProduct.getId(), customProduct.getName(), customProduct.getCategory(), productRealPrice,
                                customProduct.getMaxPersonalizations(), customizations
                        );
                    }

                    if (discountPerUnit > 0.0) {
                        System.out.printf("  %s **discount -%.3f\n", productDisplay, discountPerUnit);
                    } else {
                        System.out.println("  " + productDisplay);
                    }
                }
            }
        }

        double finalPrice = totalPrice - totalDiscount;
        System.out.println("  Total price: " + totalPrice);
        System.out.println("  Total discount: " + totalDiscount);
        System.out.println("  Final Price: " + finalPrice);
    }



    // -------------------- metodos para showTicket ---------------------
    private Map<Category, Integer> calculateCategoryCounts() {
        Map<Category, Integer> categoryCount = new HashMap<>();
        for (TicketItem item : items) {
            if (item.getProduct() instanceof BasicProduct basicProduct) {
                categoryCount.put(basicProduct.getCategory(),
                        categoryCount.getOrDefault(basicProduct.getCategory(), 0) + item.getQuantity()
                );
            }
        }
        return categoryCount;
    }

   private List<TicketItem> sortItemsByName() {
       List<TicketItem> ticketList = new ArrayList<>(items);
       ticketList.sort(Comparator.comparing(item -> item.getProduct().getName()));
       return ticketList;
   }

    // ----------------------------------------------------------------------

    public void closeTicket() {
        if (status == Status.CLOSE)  throw new IllegalStateException("Ticket already closed");
        status = Status.CLOSE;
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        id += "-" + LocalDateTime.now().format(timeFormat);
    }

    public String getId() { return id; }

    public Status getStatus() { return status; }

    public String getCashierId() { return cashierId; }

    public String getUserId() { return userId; }

    public Product getProduct(String productId) {
        for (TicketItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                return item.getProduct();
            }
        }
        return null;
    }
}
