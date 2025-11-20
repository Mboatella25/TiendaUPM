package Controllers;

import Models.Cashier;
import Models.User;
import Models.Client;
import Models.Cashier;
import Repository.UserRepository;

import java.util.*;

public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ---------------- Clients ----------------
    public void addClient(String name, String dni, String email, String cashierId) {
        boolean ok = userRepository.addClient(name, dni, email, cashierId);
        if (ok) {
            System.out.println(userRepository.getClient(dni));
            System.out.println("client add: ok");
        } else {
            System.out.println("Error: cashier not found or client already exists.");
        }
    }

    public void removeClient(String dni) {
        Client clientRemove = userRepository.getClient(dni);
        boolean ok = userRepository.removeClient(dni);
        if (ok) {
            System.out.println(clientRemove);
            System.out.println("client remove: ok");
        } else {
            System.out.println("Client not found.");
        }
    }

    public Collection<Client> listClients() {
        System.out.println("Client:");
        return userRepository.listClients();
    }

    public Client getClient(String dni) {
        return userRepository.getClient(dni);
    }

    // ---------------- Cashiers ----------------
    public void addCashier(String name, String email, String id) {
        Cashier cashier = userRepository.addCashier(name, email, id);
        if (cashier != null) {
            System.out.println(cashier);
            System.out.println("cash add: ok");
        } else {
            System.out.println("Error: cashier ID already exists.");
        }
    }

    public void removeCashier(String id) {
        Cashier cashierRemove = userRepository.getCashier(id);
        boolean ok = userRepository.removeCashier(id);
        if (ok) {
            System.out.println(cashierRemove);
            System.out.println("cash remove: ok");
        } else {
            System.out.println("Cashier not found.");
        }
    }

    public Collection<Cashier> listCashiers() {
        return userRepository.listCashiers();
    }

    public Cashier getCashier(String id) {
        return userRepository.getCashier(id);
    }
}