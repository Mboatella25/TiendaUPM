package Repository;
import Models.User;
import Models.Client;
import Models.Cash;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

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

    public boolean existsClient(String dni) {
        return users.containsKey(dni) && users.get(dni) instanceof Client;
    }

    public boolean existsCashier(String cashId) {
        return users.containsKey(cashId) && users.get(cashId) instanceof Cash;
    }

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

}