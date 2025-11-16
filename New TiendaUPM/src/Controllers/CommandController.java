package Controllers;

import ProductModels.Product;
import Models.Client;
import Models.User;
import Models.*;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CommandController {

    private final ProductController productController;
    private final TicketController ticketController;
    private final UserController userController;

    public CommandController (ProductController productController, TicketController ticketController, UserController userController){
        this.userController = userController;
        this.productController = productController;
        this.ticketController = ticketController;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ticket module app");
        System.out.println("Ticket module: Write 'help' for commands.");

        boolean running = true;
        while (running) {
            System.out.println("tUPM> "); //prompt
            String input = scanner.nextLine();
            if (input.isBlank()) continue;

            if (input.equalsIgnoreCase("exit")) {
                running = false;
                continue;
            }

            try {
                process(input);
            } catch (Exception e) {
                System.out.println("Error trying to procces the command.");
            }
        }

        System.out.println("Closing app.\n" +
                "Goodbye!");
        scanner.close();
        System.out.println("App closed.");
    }

    private void process(String input) {
        String[] parts = input.split(" ");
        switch (parts[0].toLowerCase()) {
            case "help" -> showHelp();
            case "echo" -> handleEcho(parts);
            case "prod" -> handleProd(parts, input);
            case "ticket" -> handleTicket(parts, input);
            case "client" -> handleClient(parts, input);
            case "cash" -> handleCash (parts, input);
            default -> System.out.println("Unknown command. Type 'help'.");
        }
    }

    private void showHelp() {
        System.out.println("""
                Available commands:
                > prod add <id> "<name>" <category> <price> [<maxPers>]
                > prod addFood <id> "<name>" <prize> <expiration yyyy-MM-dd> <max_people>
                > prod addMeeting  <id> "<name>" <prize> <date yyyy-MM-dd> <max_people>
                > prod list
                > prod update <id> NAME|CATEGORY|PRICE <value>
                > prod remove <id>
                > ticket new <id> <cashId> <userId>
                > ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> ...]
                > ticket remove <ticketId> <cashId> <prodId>
                > ticket print <ticketId> <cashId>
                > client add "<name>" <DNI> <email> <cashId>
                > client remove <DNI>
                > client list
                > cash add [<id>] "<name>" <email>
                > cash remove <id>
                > cash list
                > cash tickets <id>
                > echo "<text>"
                > help
                > exit
                
                Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                Discount if there is more than 2 units of the same category:
                MERCH 0%, PAPELERIA 5%, ROPA 7%, LIBRO 10%, ELECTRONICA 3%.
                """);
    }

    private void handleEcho(String[] parts) {
        if(parts.length > 1){
            System.out.println(String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)));
        }else {
            System.out.println();
        }
    }

    //PRODUCT COMMANDS
    private void handleProd(String[] parts, String fullInput) {
        switch (parts[1].toLowerCase()) {
            case "add" -> {
                String id = parts[2].equals("null") ? null : parts[2];
                String name = extractQuotedText(fullInput);
                String[] allParts = fullInput.split(" ");
                String category = allParts[allParts.length - 2];
                double price = Double.parseDouble(allParts[allParts.length - 1]);
                //maxPers -> CustomProduct
                Integer maxCustomizations = null;
                List<String> allowedTexts = null;

                //Verificar si hay parámetros adicionales
                if (allParts.length > 6 && allParts[3].matches("\\d+")) {
                    maxCustomizations = Integer.parseInt(allParts[3]);
                    allowedTexts = new ArrayList<>(); // Lista vacía por defecto
                }
                productController.addProduct(id, name, category, price, maxCustomizations, allowedTexts);
            }
            case "addfood" -> {
                String id = parts[2].equals("null") ? null : parts[2];
                String name = extractQuotedText(fullInput);
                String[] allParts = fullInput.split(" ");
                double price = Double.parseDouble(allParts[allParts.length - 3]);
                LocalDate expiration = LocalDate.parse(allParts[allParts.length - 2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int maxPeople = Integer.parseInt(allParts[allParts.length - 1]);
                productController.addFoodProduct(id, name, price,  expiration.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxPeople);
            }
            case "addmeeting" -> {
                String id = parts[2].equals("null") ? null : parts[2];
                String name = extractQuotedText(fullInput);
                String[] allParts = fullInput.split(" ");
                double price = Double.parseDouble(allParts[allParts.length - 3]);
                LocalDate meetingDate = LocalDate.parse(allParts[allParts.length - 2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int maxPeople = Integer.parseInt(allParts[allParts.length - 1]);
                productController.addMeetingProduct(id, name, price, meetingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxPeople);
            }
            case "list" -> productController.listProducts();
            case "update" -> {
                String id = parts[2];
                String field = parts[3];
                String value = field.equalsIgnoreCase("name")
                        ? extractQuotedText(fullInput)
                        : parts[4];
                productController.updateProduct(id, field, value);
            }
            case "remove" -> {
                String id = parts[2];
                System.out.println(productController.getProduct(id));
                productController.removeProduct(id);
            }
            default -> System.out.println("Usage: prod [add|list|update|remove]");
        }
    }

    //TICKET COMMANDS
    private void handleTicket(String[] parts, String fullInput) {
        switch (parts[1].toLowerCase()) {
            case "new" -> {
                String ticketId = parts[2].equals("null") ? null : parts[2];
                String cashId = parts[3];
                String userId = parts[4];
                ticketController.createTicket(ticketId, cashId, userId);
            }
            case "add" -> {
                String ticketId = parts[2];
                String cashId = parts[3];
                String prodId = parts[4];
                int amount = Integer.parseInt(parts[5]);

                List<String> customizations = Arrays.stream(parts)
                        .filter(p -> p.startsWith("--p"))
                        .map(p -> p.substring(3))
                        .collect(Collectors.toList());

                ticketController.addProductToTicket(ticketId, cashId, prodId, amount, customizations);
            }
            case "remove" -> {
                String ticketId = parts[2];
                String cashId = parts[3];
                String prodId = parts[4];
                ticketController.removeProductFromTicket(ticketId, cashId, prodId);
            }
            case "print" -> {
                String ticketId = parts[2];
                String cashId = parts[3];
                ticketController.printTicket(ticketId, cashId);
            }
            case "list" ->
                    ticketController.listTickets();
            default -> System.out.println("Usage: ticket [new|add|remove|print]");
        }
    }

    //CLIENT COMMANDS
    private void handleClient(String[] parts, String fullInput) {
        switch (parts[1].toLowerCase()) {
            case "add" -> {
                String name = extractQuotedText(fullInput);
                String dni = parts[parts.length - 3];
                String email = parts[parts.length - 2];
                String cashId = parts[parts.length - 1];
                System.out.println(userController.addClient(dni, name, email, cashId));
            }
            case "remove" -> {
                String dni = parts[2];
                System.out.println(userController.removeClient(dni));
            }
            case "list" -> {
                List<Client> clients = userController.listClients();
                clients.forEach(System.out::println);
            }
            default -> System.out.println("Usage: client [add|remove|list]");
        }
    }

    //CASH COMMANDS
    private void handleCash(String[] parts, String fullInput) {
        switch (parts[1].toLowerCase()) {
            case "add" -> {
                String name = extractQuotedText(fullInput);
                String email = parts[parts.length - 1];
                String id = parts.length > 4 ? parts[2] : null; // optional id
                System.out.println(userController.addCash(id, name, email));
            }
            case "remove" -> {
                String id = parts[2];
                System.out.println(userController.removeCash(id));
            }
            case "list" -> {
                List<Cash> cashiers = userController.listCash();
                cashiers.forEach(System.out::println);
            }
            case "tickets" -> {
                String id = parts[2];
                List<Ticket> tickets = userController.getCashTickets(id); // Implementa este método en UserController
                tickets.forEach(t -> System.out.println(t.getId() + " " + t.getEstado()));
            }
            default -> System.out.println("Usage: cash [add|remove|list|tickets]");

        }
    }

    // Para devolver el nombre del producto dentro de las comillas
    private String extractQuotedText(String input) {
        int first = input.indexOf('"');
        int last = input.lastIndexOf('"');
        return input.substring(first + 1, last);
    }
}
