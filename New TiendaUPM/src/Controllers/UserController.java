package Controllers;

import Models.User;
import Models.Client;
import Models.Cash;
import Repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //CLientes
    public String addClient(String dni, String name, String email, String cashId){
        try{

            //Verificar que el cajero existe
            if(!userRepository.exitsCashier(cashId)){
                return "Error: El cajero con ID " + cashId + " no existe";
            }

            //Verificar que el DNI no existe
            if(userRepository.existsClient(dni)){
                return "Error: El cliente con ID " + dni + " ya existe";
            }

            Client client = new Client(dni, name, email, cashId);
            userRepository.addUser(client);
            return "Cliente añadido: " + dni;
        }catch (IllegalArgumentException e){
            return "Error: " + e.getMessage();
        }

    }
    public String removeClient(String dni){
       try{
           if(!userRepository.exitsClient(dni)){
               return "Error: El cliente con ID " + dni + " no existe";
           }
           userRepository.removeUser(dni);
           return "Cliente eliminado: " + dni;
       }catch (IllegalArgumentException e){
           return "Error: " + e.getMessage();
       }
    }
    public List<Client> listClients(){
        List <Client> clients = userRepository.getAllClients();
        clients.sort(Comparator.comparing(Client::getName));
        return clients;
    }

    //Cajeros
    public String addCash(String dni, String name, String email){
        try{
            Cash cash = new Cash(dni, name, email);

            //Verificamos que el Id sea unico
            if (userRepository.existsCashier(cash.getId())){
                return "Error: El cajero con ID " + cash.getId() + " ya existe";
            }
            userRepository.addUser(cash);
            return "Cajero añadido: " + cash.getId();
        }catch(IllegalArgumentException e){
            return "Error: " + e.getMessage();
        }
    }

    public String removeCash(String id){
        try{
            if(!userRepository.existsCashier(id)){
                return "Error: El cajero con ID " + id + " no existe";
            }
            userRepository.removeUser(id);
            return "Cajero eliminado: " + id;
        }catch(IllegalArgumentException e){
            return "Error: " + e.getMessage();
        }
    }

    public List<Cash> listCash(){
        List<Cash> cashiers = userRepository.getAllCash();
        cashiers.sort(Comparator.comparing(Cash::getName));
        return cashiers;
    }

    public boolean cashierExists(String cashId){
        return userRepository.existsCashier(cashId);
    }

    public boolean clientExists(String dni){
        return userRepository.existsClient(dni);
    }
}
