package Controllers;

import Models.Cashier;
import Models.Category;
import Models.Client;
import Models.Ticket;
import ProductModels.*;
import Repository.ProductRepository;
import Repository.TicketRepository;
import Repository.UserRepository;

import java.time.LocalDate;
import java.util.*;

public class CommandController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TicketRepository ticketRepository;

    private final UserController userController;
    private final ProductController productController;
    private final TicketController ticketController;
    private final Scanner scanner = new Scanner(System.in);

    public CommandController() {
        this.userRepository = new UserRepository();
        this.productRepository = new ProductRepository();
        this.ticketRepository = new TicketRepository(userRepository, productRepository);

        this.userController = new UserController(userRepository);
        this.productController = new ProductController(productRepository);
        this.ticketController = new TicketController(ticketRepository);
    }

    public void run() {
        System.out.println("Welcome to the ticket module app");
        System.out.println("Ticket module. Type 'help' to see commands.");

        while (true) {
            System.out.print("tUPM> "); //prompt
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            process(input);
        }
    }

    private void process(String input) {
        String[] commandParts = input.split("\\s+");
        try {
            switch (commandParts[0].toLowerCase()) {
                case "client" -> {
                    handleClient(commandParts);
                    System.out.println();
                }
                case "cash" -> {
                    handleCashier(commandParts);
                    System.out.println();
                }
                case "ticket" -> {
                    handleTicket(commandParts);
                    System.out.println();
                }
                case "prod" -> {
                    handleProduct(commandParts);
                    System.out.println();
                }
                case "help" -> {
                    printHelp();
                    System.out.println();
                }
                case "echo" -> {
                    handleEcho(commandParts);
                    System.out.println();
                }
                case "exit" -> {
                    System.out.println("Closing application.");
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Unknown command: Type 'help'.");
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    // ---------------- CLIENT COMMANDS ----------------
        private void handleClient(String[] commandParts) {
            if (commandParts.length < 2) {
                System.out.println("Invalid client command");
                return;
            }
            switch (commandParts[1].toLowerCase()) {
                case "add" -> {
                    if (commandParts.length < 6) { System.out.println("Usage: client add \"<name>\" <DNI> <email> <cashId>"); return; }
                    String name = commandParts[2];
                    String dni = commandParts[3];
                    String email = commandParts[4];
                    String cashId = commandParts[5];
                    userController.addClient(name, dni, email, cashId);
                }
                case "remove" -> {
                    if (commandParts.length < 3) { System.out.println("Usage: client remove <DNI>"); return; }
                    userController.removeClient(commandParts[2]);
                }
                case "list" -> {
                    Collection<Client> clients = userController.listClients();
                    for (Client client : clients) {
                        System.out.println(client);
                    }
                    System.out.println("client list: ok");
                }
                default -> System.out.println("Unknown client command: " + commandParts[1]);
            }
        }

    // ---------------- CASHIER COMMANDS ----------------
        private void handleCashier(String[] commandParts) {
            if (commandParts.length < 2) { System.out.println("Invalid cash command"); return; }
            switch (commandParts[1].toLowerCase()) {
                case "add" -> {
                    String id = null;
                    String name;
                    String email;

                    if (commandParts.length == 5) {
                        id = commandParts[2];
                        name = extractQuotedText(commandParts, 3);
                        email = commandParts[4];
                    } else if (commandParts.length == 4) {
                        name = extractQuotedText(commandParts, 2);
                        email = commandParts[3];
                    } else {
                        System.out.println("Usage: cash add [<id>] \"<name>\" <email>");
                        return;
                    }
                    userController.addCashier(name, email, id);
                }
                case "remove" -> {
                    if (commandParts.length < 3) {
                        System.out.println("Usage: cash remove <id>");
                        return;
                    }
                    String cashierId = commandParts[2];
                    userController.removeCashier(cashierId);
                    ticketController.removeTicketsFromCashier(cashierId);
                }
                case "list" -> {
                    Collection<Cashier> cashiers = userController.listCashiers();
                    for (Cashier cashier : cashiers) {
                        System.out.println(cashier);
                    }
                    System.out.println("cash list: ok");
                }
                case "tickets" -> {
                    if (commandParts.length < 3) {
                        System.out.println("Usage: cash tickets <id>");
                        return;
                    }
                    ticketController.listTicketsByCashier(commandParts[2]);
                }
                default -> System.out.println("Unknown cash command: " + commandParts[1]);
            }
        }

    // ---------------- TICKET COMMANDS -----------------
    private void handleTicket(String[] commandParts) {
        if (commandParts.length < 2) { System.out.println("Invalid ticket command"); return; }
        switch (commandParts[1].toLowerCase()) {
            case "new" -> {
                String id = null;
                String cashierId;
                String userId;
                if (commandParts.length == 5) {
                    id = commandParts[2]; cashierId = commandParts[3]; userId = commandParts[4];
                } else if (commandParts.length == 4) {
                    cashierId = commandParts[2]; userId = commandParts[3];
                } else { System.out.println("Usage: ticket new [<id>] <cashId> <userId>"); return; }
                ticketController.createTicket(id, cashierId, userId);
            }
            case "add" -> {
                if (commandParts.length < 6) { System.out.println("Usage: ticket add <ticketId> <cashId> <prodId> <amount> [--p <txt>]"); return; }
                String ticketId = commandParts[2];
                String cashierId = commandParts[3];
                String productId = commandParts[4];
                int amount = Integer.parseInt(commandParts[5]);

                List<String> customizations = new ArrayList<>();
                for (int index = 6; index < commandParts.length; index++) {
                    if (commandParts[index].startsWith("--p")) customizations.add(commandParts[index].substring(3));
                }
                ticketController.addProductToTicket(ticketId, cashierId, productId, amount, customizations);
            }
            case "remove" -> {
                if (commandParts.length < 5) {
                    System.out.println("Usage: ticket remove <ticketId> <cashId> <prodId>");
                    return;
                }
                String ticketId = commandParts[2];
                String cashierId = commandParts[3];
                String prodId = commandParts[4];
                ticketController.removeProductFromTicket(ticketId, cashierId, prodId);
            }
            case "print" -> {
                if (commandParts.length < 4) { System.out.println("Usage: ticket print <ticketId> <cashId>"); return; }
                ticketController.printTicket(commandParts[2]);
            }
            case "list" -> {
                ticketController.listTickets();
            }
            default -> System.out.println("Unknown ticket command: " + commandParts[1]);
        }
    }

    // ---------------- PRODUCT COMMANDS ----------------
    private void handleProduct(String[] commandParts) {
        if (commandParts.length < 2) {
            System.out.println("Usage: prod <add|update|addFood|addMeeting|list|remove>");
            return;
        }
        switch (commandParts[1].toLowerCase()) {
            case "add" -> {
                if (commandParts.length < 5) {
                    System.out.println("Usage: prod add [<id>] \"<name>\" <category> <price> [<maxPers>]");
                    return;
                }

                int index = 2;
                String id = "";

                if (!commandParts[index].startsWith("\"")) {
                    id = commandParts[index];
                    index++;
                }

                String name = extractQuotedText(commandParts, index);
                while (!commandParts[index].endsWith("\"")) index++;
                index++;

                Category category = Category.valueOf(commandParts[index].toUpperCase());
                index++;

                double price = Double.parseDouble(commandParts[index]);
                index++;

                // Producto personalizable
                if (index < commandParts.length) {
                    int maxPersonalizations = Integer.parseInt(commandParts[index]);
                    CustomProduct product = new CustomProduct(id, name, category, price, maxPersonalizations);
                    productController.addProduct(product);
                } else {
                    // Producto básico
                    BasicProduct product = new BasicProduct(id, name, category, price);
                    productController.addProduct(product);
                }
            }
            case "update" -> {
                if (commandParts.length < 5)
                    System.out.println("Uso: prod update <id> NAME|CATEGORY|PRICE <value>");

                String id = commandParts[2];
                String field = commandParts[3];
                String value;

                if (field.equalsIgnoreCase("NAME")) {
                    value = extractQuotedText(commandParts, 4); // si tiene espacios
                } else {
                    value = commandParts[4];
                }

                productController.updateProduct(id, field, value);
            }
            case "addfood" -> {
                if (commandParts.length < 7)
                    System.out.println("Usage: prod addFood [<id>] \"<name>\" <price> <expiration yyyy-MM-dd> <max_people>");

                int index = 2;
                String id = "";

                if (!commandParts[index].startsWith("\"")) {
                    id = commandParts[index];
                    index++;
                }

                String name = extractQuotedText(commandParts, index);
                while (!commandParts[index].endsWith("\"")) index++;
                index++;

                double price = Double.parseDouble(commandParts[index]);
                index++;

                LocalDate expiration;
                expiration = LocalDate.parse(commandParts[index]);
                index++;

                int maxPeople = Integer.parseInt(commandParts[index]);

                FoodProduct product = new FoodProduct(id, name, price, expiration, maxPeople);
                productController.addProduct(product);
            }
            case "addmeeting" -> {
                if (commandParts.length < 7)
                    System.out.println("Usage: prod addMeeting [<id>] \"<name>\" <price> <expiration:yyyy-MM-dd> <max_people>");

                int index = 2;
                String id = "";

                if (!commandParts[index].startsWith("\"")) {
                    id = commandParts[index];
                    index++;
                }

                String name = extractQuotedText(commandParts, index);
                while (!commandParts[index].endsWith("\"")) index++;
                index++;

                double price = Double.parseDouble(commandParts[index]);
                index++;

                LocalDate expiration = LocalDate.parse(commandParts[index]);
                index++;

                int maxPeople = Integer.parseInt(commandParts[index]);

                MeetingProduct prod = new MeetingProduct(id, name, price, expiration, maxPeople);
                productController.addProduct(prod);
            }
            case "list" -> {
                productController.listProducts();
            }
            case "remove" -> {
                if (commandParts.length < 3) System.out.println("Usage: prod remove <id>");
                String id = commandParts[2];
                productController.removeProduct(id);
            }
            default -> System.out.println("Unknown command.");
        }
    }

    // ---------------- GENERAL COMMANDS ----------------
    private void printHelp() {
        System.out.println("""
                Commands:
                  client add "<nombre>" <DNI> <email> <cashId>
                  client remove <DNI>
                  client list
                  cash add [<id>] "<nombre>"<email>
                  cash remove <id>
                  cash list
                  cash tickets <id>
                  ticket new [<id>] <cashId> <userId>
                  ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]
                  ticket remove <ticketId><cashId> <prodId>
                  ticket print <ticketId> <cashId>
                  ticket list
                  prod add <id> "<name>" <category> <price>
                  prod update <id> NAME|CATEGORY|PRICE <value>
                  prod addFood [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                  prod addMeeting [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                  prod list
                  prod remove <id>
                  help
                  echo “<text>”
                  exit
                
                Categories: [MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS]
                Discount if there are >= 2 units in the category:
                MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.
                """);
    }

    private void handleEcho(String[] commandParts) {
        if(commandParts.length > 1){
            System.out.println(String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length)));
        } else {
            System.out.println();
        }
    }

    // Para devolver el nombre del producto dentro de las comillas
    private String extractQuotedText(String[] commandParts, int startIndex) {
        if (!commandParts[startIndex].startsWith("\""))
            throw new IllegalArgumentException("Name must be in quotation marks.");
        StringBuilder stringBuilder = new StringBuilder(commandParts[startIndex].substring(1));

        while (!commandParts[startIndex].endsWith("\"")) {
            startIndex++;
            stringBuilder.append(" ").append(commandParts[startIndex]);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}

