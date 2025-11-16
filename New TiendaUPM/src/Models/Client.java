package Models;

public final class Client extends User {
    //atributos especificos del cliente: quien lo dio de alta (cash id)
    private final String cashId;

    public Client(String clientId, String nombre, String email, String cashId) {
        super(clientId, nombre, email);
        if (cashId == null || cashId.isBlank())
            throw new IllegalArgumentException("El cashId es necesario");
        this.cashId = cashId;
    }
    public String getCashId() {
        return cashId;
    }

    @Override
    public String toString() {
        return String.format("{class:Client, id: %s, nombre: %s, email: %s, cashId: %s}", getId(), getName(), getEmail(), getCashId());
    }

}
