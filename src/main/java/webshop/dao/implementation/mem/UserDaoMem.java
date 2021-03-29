package webshop.dao.implementation.mem;

import webshop.dao.CartDao;
import webshop.dao.DataManager;
import webshop.model.exception.DataNotFoundException;
import webshop.dao.UserDao;
import webshop.model.Cart;
import webshop.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class UserDaoMem implements UserDao {

    private List<User> users = new ArrayList<>();

    @Override
    public void add(User user) {
        if (isNameAvailable(user.getName())) {
            CartDao cartDao = DataManager.getCartDao();
            Cart cart = new Cart();
            cartDao.add(cart);

            user.setCartId(cart.getId());

            user.setId(users.isEmpty() ? 1 : users.get(users.size() - 1).getId() + 1);
            users.add(user);
        }
    }

    @Override
    public User find(String name, String password) {
        return users
                .stream()
                .filter(user -> user.getName().equals(name) && BCrypt.checkpw(password, user.getPassword()))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such user"));
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public boolean isNameAvailable(String name) {
        return users
                .stream()
                .map(User::getName)
                .noneMatch(username -> username.equals(name));
    }
}