package Repository;
import Models.User;
import Models.Client;
import Models.Cash;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final List<User> users = new ArrayList<>();
    //Añadir un usuario (Cliente o Cajero) y asegurar que son únicos
    public void addUser(User user) {
        if (getUserbyId(user.getId()).isPresent()){
            throw new IllegalArgumentException("Usuario con ID " + user.getId() + " ya existe");
        }
        users.add(user);
    }
    //Eliminar al usuario por su ID
    public void removeUser(String id) {
        users.removeIf(user -> user.getId().equals(id));
    }
    //Get usar by Id
    public Optional<User> getUserbyId(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    //Get all clients
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Client) {
                clients.add((Client) u);
            }
        }
        return clients;
    }
    //Get all cashiers
    public List<Cash> getAllCash() {
        List<Cash> cashes = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Cash) {
                cashes.add((Cash) u);
            }
        }
        return cashes;
    }
}
