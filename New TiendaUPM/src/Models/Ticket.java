package Models;

import ProductModels.CustomProduct;
import ProductModels.FoodProduct;
import ProductModels.MeetingProduct;
import ProductModels.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Ticket {

    public enum Estado{
        VACIO, ACTIVO, CERRADO
    }

    private String id;
    private Estado estado;
    private final String clientId; //Solo se guarda el id, no el objeto
    private final String cashId;
    private final LocalDateTime creationDate;
    private LocalDateTime closeDate;
    private static final int maxProducts = 100;
    private Map<String, TicketItem> items = new HashMap<>(); //Key: productId

    //Para garantizar IDs únicos
    private static final Set<String> usedIds = ConcurrentHashMap.newKeySet();

    //Constructors
    //VER SI ELIMINAR
    public Ticket(){
        //initialize default values
        this.clientId = "";
        this.cashId = "";
        this.creationDate = LocalDateTime.now();
        this.id = generateId();
        this.estado = Estado.VACIO;
        this.items = new HashMap<>();
    }

    public Ticket(String id, String cashtId, String clientId) {
        if (clientId == null || clientId.isBlank())
            throw new IllegalArgumentException("Client ID required");
        if (cashtId == null || cashtId.isBlank())
            throw new IllegalArgumentException("CashtId required");

        this.clientId = clientId;
        this.cashId = cashtId;
        this.estado = Estado.VACIO;
        this.creationDate = LocalDateTime.now();
        this.items = new HashMap<>();

        //Generar o validar ID
        if (id == null || id.isBlank()){
            this.id = generateId();
        } else {
            if (usedIds.contains(id))
                throw new IllegalArgumentException("Ticket already used");
            this.id = id;
        }
        usedIds.add(this.id);
    }

    //ID
    private String generateId() {
        String id;
        do {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
            String timestamp = creationDate.format(formatter);
            int randomNum = new Random().nextInt(90000) + 10000;
            id = timestamp + String.format("%05d", randomNum);
        }while (usedIds.contains(id));
        return id;
    }

    //Mark ticket as closed
    public void closeTicket(){
        if (estado == Estado.CERRADO)
            throw new IllegalStateException("Ticket already closed");

        validateTemporalProducts();

        this.estado = Estado.CERRADO;
        this.closeDate = LocalDateTime.now();
        updateIdWithCloseDate();
    }

    private void validateTemporalProducts() {
        LocalDateTime now = LocalDateTime.now();

        for (TicketItem item : items.values()) {

            Product p = item.getProduct();

            //Si es FoodProduct
            if (p instanceof FoodProduct){
                FoodProduct food = (FoodProduct) p;
                //Validar que no está caducado
                if (food.getExpirationDate().isBefore(LocalDate.now())){
                    throw new IllegalStateException("No se puede cerrar el ticket: la comida ya no es válida.");
                }
                //Validar 3 días de planificación
                if (creationDate.plusDays(3).isAfter(now)){
                    throw new IllegalArgumentException("Comida requiere 3 días de planificación");
                }
            }

            //Si es MeetingProduct
            if (p instanceof MeetingProduct){
                MeetingProduct meeting = (MeetingProduct) p;
                //Validar que no ha pasado
                if(meeting.getDate().isBefore(now)){
                    throw new IllegalArgumentException("No se puede cerrar el ticket: la reunión ya ha pasado.");
                }
                //Validar 12 horas de planificación
                if (creationDate.plusHours(12).isAfter(now)){
                    throw new IllegalStateException("Reunión requiere 12 h de planificación");
                }
            }
        }
    }

    //Update ID to include closing date
    private void updateIdWithCloseDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String closeTimestamp = closeDate.format(formatter);
        usedIds.remove(this.id);
        this.id += closeTimestamp;
        usedIds.add(this.id);
    }

    //Verificación de permisos
    public boolean canBeModifiedBy(String cashId){
        return this.cashId.equals(cashId) && this.estado != Estado.CERRADO;
    }

    //Getters
    public String getId() {return id;}
    public Estado getEstado() {return estado;}
    public String getClientId() {return clientId;}
    public String getCashId() {return cashId;}
    public LocalDateTime getCreationDate() {return creationDate;}
    public LocalDateTime getCloseDate() {return closeDate;}
    public Map<String, TicketItem> getItems() {return new HashMap<>(items);}


    //Item management
    public void addProduct(Product product, int quantity, List<String> customTexts) {
        if (!canBeModifiedBy(cashId))
            throw new IllegalStateException("Solo el cajero creador puede modificar el ticket");
        if (quantity <= 0)
            throw new IllegalArgumentException("Cantidad no válida");
        if (items.size() >= maxProducts)
            throw new IllegalArgumentException("Ticket lleno");

        //Validar productos únicos (comidas/reuniones)
        if ((product instanceof FoodProduct) || (product instanceof MeetingProduct)
                && items.containsKey(product.getId()))
            throw new IllegalArgumentException("El producto ya existe en el cliente");

        //Validar personalizaciones
        if (product instanceof CustomProduct){
            CustomProduct customProduct = (CustomProduct) product;
            if (customTexts.size() > customProduct.getMaxCustomizations())
                throw new IllegalArgumentException("Demasiadas personalizaciones");
            for (String text : customTexts){
                if (!customProduct.getAllowedTexts().contains(text))
                    throw new IllegalArgumentException("Texto no permitido");
            }
        }

        TicketItem existingItem = items.get(product.getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new TicketItem(product, quantity));
        }
        if (estado == Estado.VACIO) estado = Estado.ACTIVO;
    }
    //Añadir CustomProducts
    public void addProduct(CustomProduct product, int quantity, List<String> texts) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Invalid quantity.");
        if (items.size() >= maxProducts)
            throw new IllegalArgumentException("Ticket has reached the limit.");

        //Validar textos
        if (texts.size() > product.getMaxCustomizations())
            throw new IllegalArgumentException("Exceeds max allowed customizations.");
        for (String t : texts) {
            if (!product.getAllowedTexts().contains(t)){
                throw new IllegalArgumentException("Custom product is not allowed to add text " + t);
            }
        }
        TicketItem existingItem = items.get(product.getId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new TicketItem(product, quantity, texts));
        }
        if (estado == Estado.VACIO) estado = Estado.ACTIVO;
    }

    public void removeProduct(String productId) {
        if (!canBeModifiedBy(cashId))
            throw new IllegalArgumentException("Solo el cajero creador puede remover el ticket");
        items.remove(productId);
        if (items.isEmpty()) estado = Estado.VACIO;
    }

    public void clear() {
        items.clear();
        estado = Estado.VACIO;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    //Category Discounts
    public Map<Category, Integer> countProductsByCategory() {
        Map<Category, Integer> map = new HashMap<>();
        for (TicketItem item : items.values()) {
            Category c = item.getProduct().getCategory();
            map.put(c, map.getOrDefault(c, 0) + item.getQuantity());
        }
        return map;
    }

    public double calculateTotalWithDiscount() {
        double total = 0.0;
        Map<Category, Integer> counter = countProductsByCategory();

        for (TicketItem item : items.values()) {
            Product product = item.getProduct();
            double subtotal = item.getSubtotal();
            double discount = 0.0;

            if (counter.get(product.getCategory()) > 1) {
                discount = product.getCategory().getDiscount();
            }
            total += subtotal * (1 - discount);
        }

        return total;
    }


    //Printing
    public void generateTicket() {
        if (isEmpty()) {
            System.out.println("Empty ticket.");
            return;
        }

        Map<Category, Integer> counter = countProductsByCategory();

        List<TicketItem> sorted = new ArrayList<>(items.values());
        sorted.sort(Comparator.comparing(item -> item.getProduct().getName()));

        double total = 0;
        double totalDiscount = 0;

        for (TicketItem item : sorted) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            double discount = (counter.get(product.getCategory()) > 1)
                    ? product.getCategory().getDiscount()
                    : 0.0;

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
