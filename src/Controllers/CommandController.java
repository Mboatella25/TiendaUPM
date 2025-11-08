package Controllers;

import Models.Product;

import java.util.Arrays;
import java.util.Scanner;

public class CommandController {
    private ProductController productController = new ProductController();
    private TicketController ticketController = new TicketController();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ticket module app");
        System.out.println("Ticket module: Write 'help' for commands.");

        boolean running = true;
        while (running) {
            System.out.println("tUPM> "); //prompt
            String input = scanner.nextLine();

            if (input.isEmpty()) continue;

            if (input.equalsIgnoreCase("exit")) {
                running = false;
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
        switch (parts[0]) {
            case "help" -> showHelp();
            case "echo" -> handleEcho(parts);
            case "prod" -> handleProd(parts, input);
            case "ticket" -> handleTicket(parts);
            default -> System.out.println("Unknown command. Type 'help'.");
        }
    }

    private void showHelp() {
        System.out.println("""
                Available commands:
                > prod add <id> "<name>" <category> <price>
                > prod list
                > prod update <id> NAME|CATEGORY|PRICE value
                > prod remove <id>
                > ticket new
                > ticket add <prodId> <quantity>
                > ticket remove <prodId>
                > ticket print
                > echo "<text>"
                > help
                > exit
                
                Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                Discount if there is more than 2 units of the same category:
                MERCH 0%, PAPELERIA 5%, ROPA 7%, LIBRO 10%, ELECTRONICA 3%.
                """);
    }

    private void handleProd(String[] parts, String fullInput) {
        switch (parts[1]) {
            case "add" -> {
                int id = Integer.parseInt(parts[2]);
                String name = extractQuotedText(fullInput);
                String category = parts[parts.length - 2];
                double price = Double.parseDouble(parts[parts.length - 1]);
                productController.addProduct(id, name, category, price);
            }
            case "list" -> productController.listProducts();
            case "update" -> {
                int id = Integer.parseInt(parts[2]);
                String field = parts[3];
                String value;

                if (field.equalsIgnoreCase("name")) {
                    value = extractQuotedText(fullInput);
                } else {
                    value = parts[4];
                }

                System.out.println(productController.getProduct(id));
                productController.updateProduct(id, field, value);
            }
            case "remove" -> {
                int id = Integer.parseInt(parts[2]);
                System.out.println(productController.getProduct(id));
                productController.removeProduct(id);
            }
            default -> System.out.println("Usage: prod [add|list|update|remove]");
        }
    }

    private void handleTicket(String[] parts) {
        switch (parts[1]) {
            case "new" -> ticketController.newTicket();
            case "add" -> {
                int id = Integer.parseInt(parts[2]);
                int quantity = Integer.parseInt(parts[3]);
                Product p = productController.getProduct(id);
                if (p == null) throw new IllegalArgumentException("Product not found.");
                ticketController.addProduct(p, quantity);
            }
            case "remove" -> ticketController.removeProduct(Integer.parseInt(parts[2]));
            case "print" -> ticketController.printTicket();
            default -> System.out.println("Usage: ticket [new|add|remove|print]");
        }
    }

    private void handleEcho(String[] parts) {
        if(parts.length > 1){
            System.out.println(String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)));
        }else {
            System.out.println();
        }
    }

    // Para devolver el nombre del producto dentro de las comillas
    private String extractQuotedText(String input) {
        int first = input.indexOf('"');
        int last = input.lastIndexOf('"');
        return input.substring(first + 1, last);
    }
}
