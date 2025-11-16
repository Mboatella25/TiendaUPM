package Repository;

import Models.User;
import Models.Client;
import Models.Cash;
import Models.Ticket;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Usuario con ID " + user.getId() + " ya existe");
        }
        users.put(user.getId(), user);
    }

    public void removeUser(String id) {
        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("Usuario con ID " + id + " no existe");
        }
        users.remove(id);
    }

    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    //Verificación de existencia
    public boolean existsClient(String clientId) {
        return users.containsKey(clientId) && users.get(clientId) instanceof Client;
    }

    public boolean existsCashier(String cashId) {
        return users.containsKey(cashId) && users.get(cashId) instanceof Cash;
    }

    //Métodos para obtener colecciones
    public List<Client> getAllClients() {
        return users.values().stream()
                .filter(user -> user instanceof Client)
                .map(user -> (Client) user)
                .collect(Collectors.toList());
    }

    public List<Cash> getAllCash() {
        return users.values().stream()
                .filter(user -> user instanceof Cash)
                .map(user -> (Cash) user)
                .collect(Collectors.toList());
    }

    //Busquedas eficientes
    public Optional<Cash> getCashById(String cashId) {
        User user = users.get(cashId);
        return (user instanceof Cash) ? Optional.of((Cash) user) : Optional.empty();
    }

    public Optional<Client> getClientById(String clientId) {
        User user = users.get(clientId);
        return (user instanceof Client) ? Optional.of((Client) user) : Optional.empty();
    }
    //Obtener tickets de un cajero específico
    public List<Ticket> getCashTickets(String cashId) {
        Optional<Cash> cashOpt = getCashById(cashId);
        return cashOpt.map(Cash::getTickets).orElse(Collections.emptyList());
    }
    //Contar total de usuarios:
    public int getUserCount() {
        return users.size();
    }
    //Verificar si existe cualquier ususario con el ID
    public boolean existsUser(String id) {
        return users.containsKey(id);
    }
}