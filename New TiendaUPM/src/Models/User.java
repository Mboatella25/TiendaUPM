package Models;

public abstract class User {
    private final String id; //DNI o UW
    private final String name;
    private final String email;

    protected User(String id, String name, String email) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Invalid id");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Invalid name");
        if (email == null || email.isBlank() || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        this.id = id;
        this.name = name;
        this.email = email;
    }

    //Solo getters: los atributos son inmutables
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }


    @Override
    public String toString() {
        return String.format("{class:User, id:%s, name:'%s', email:%s}", id, name, email);
    }

}
