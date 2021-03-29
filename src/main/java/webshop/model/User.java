package webshop.model;

import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

@Getter
public class User extends BaseModel {
    private final String name;
    private final String password;
@Setter
    private int cartId;

    public User(String name, String password) {
        this.name = name;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User(int id, String name, String password, int cartId) {
        this.name = name;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.id = id;
        this.cartId = cartId;
    }

    public User(int id, String name, String password, int cartId, boolean notEncrypted) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.cartId = cartId;
    }
}