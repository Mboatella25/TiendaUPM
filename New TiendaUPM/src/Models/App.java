package Models;

import Controllers.CommandController;

public class App {
    public static void main(String[] args) {
        CommandController commandController = new CommandController();
        commandController.start();
    }
}
