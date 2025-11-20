package Models;

public final class Client extends User {
    private final String cashId; //atributos especificos del cliente: quien lo dio de alta (cash id)

    public Client(String DNI, String nombre, String email, String cashId) {
        super(DNI, nombre, email);
        if (cashId == null || cashId.isBlank())
            throw new IllegalArgumentException("The cashId is necessary");
        this.cashId = cashId;
    }
    public String getCashierId() {
        return cashId;
    }


    @Override
    public String toString() {
        return String.format("client{identifier:'%s', name:'%s', email:'%s', cash:'%s'}", getId(), getName(), getEmail(), getCashierId());
    }

}