package Controllers;

import Models.User;
import Models.Client;
import Models.Cash;
import Repository.UserRepository;

import java.util.List;

public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //CLientes
    public void addClient(String dni, String name, String email, String cashId){
        Client client = new Client(dni, name, email, cashId);
        userRepository.addUser(client);
    }
    public void removeClient(String dni){
        userRepository.removeUser(dni);
    }
    public List<Client> listClients(){
        return userRepository.getAllClients();
    }

    //Cajeros
    public void addCash(String dni, String name, String email){
        Cash cash = new Cash(dni, name, email);
        userRepository.addUser(cash);
    }
    public void removeCash(String dni){
        userRepository.removeUser(dni);
    }
    public List<Cash> listCash(){
        return userRepository.getAllCash();
    }
}
