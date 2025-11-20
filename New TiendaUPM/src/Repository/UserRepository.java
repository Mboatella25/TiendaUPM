package Repository;
import Models.User;
import Models.Client;
import Models.Cashier;

import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserRepository {
    private final Map<String, Client> clients = new HashMap<>();
    private final Map<String, Cashier> cashiers = new HashMap<>();
    private final Set<String> cashiersUsedIds = ConcurrentHashMap.newKeySet(); // No se permiten duplicados


    // ------------------------- Cients ----------------------------
    public boolean addClient(String name, String dni, String email, String cashierId) {
        if (!cashiers.containsKey(cashierId)) return false;
        if (clients.containsKey(dni)) return false;

        Client client = new Client(dni, name, email, cashierId);
        clients.put(dni, client);
        return true;
    }

    public boolean removeClient(String dni) {
        return clients.remove(dni) != null;
    }

    public Collection<Client> listClients() {
        return clients.values().stream()
                .sorted(Comparator.comparing(Client::getName))
                .collect(Collectors.toList());
    }

    public Client getClient(String dni) {
        return clients.get(dni);
    }

    // ---------------------- Cashiers --------------------------
    public Cashier addCashier(String name, String email, String id) {
        if (id == null || id.isEmpty()) {
            id = generateCashierId();
        }
        if (cashiers.containsKey(id)) return null;

        Cashier cashier = new Cashier(id, name, email);
        cashiers.put(id, cashier);
        cashiersUsedIds.add(id);
        return cashier;
    }

    public boolean removeCashier(String id) {
        cashiersUsedIds.remove(id);
        return cashiers.remove(id) != null;
    }

    public Collection<Cashier> listCashiers() {
        return cashiers.values().stream()
                .sorted(Comparator.comparing(Cashier::getName))
                .collect(Collectors.toList());
    }

    public Cashier getCashier(String id) {
        return cashiers.get(id);
    }

    private String generateCashierId() {
        Random random = new Random();
        String id;
        do {
            id = "UW" + String.format("%07d", random.nextInt(10000000));
        } while (cashiersUsedIds.contains(id));
        cashiersUsedIds.add(id);
        return id;
    }
}