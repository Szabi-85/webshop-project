package webshop.dao.implementation.file;

import webshop.dao.CartDao;
import webshop.dao.DataManager;
import webshop.dao.UserDao;
import webshop.model.Cart;
import webshop.model.User;
import webshop.model.exception.DataNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UserDaoCsvFile implements UserDao {
    private static String FILE_PATH = "test/users.csv";

    @Override
    public void add(User user) {
        if (isNameAvailable(user.getName())) {
            CartDao cartDao = DataManager.getCartDao();
            Cart cart = new Cart();
            cartDao.add(cart);

            user.setCartId(cart.getId());

            List<List<String>> data = EasyFile.importFromCSV(FILE_PATH);
            user.setId(data.size() == 0 ? 1 : Integer.parseInt(data.get(data.size() - 1).get(0)) + 1);
            data.add(Arrays.asList(
                    String.valueOf(user.getId()),
                    user.getName(),
                    user.getPassword(),
                    String.valueOf(user.getCartId())
            ));
            EasyFile.exportToCSV(FILE_PATH, data);
        }
    }

    @Override
    public User find(String userName, String password) {
        if (!Files.exists(Paths.get(FILE_PATH))) {
            throw new DataNotFoundException("No such user found");
        }
        return EasyFile.importFromCSV(FILE_PATH).stream()
                .filter(line -> line.get(1).equals(userName) && BCrypt.checkpw(password, line.get(2)))
                .map(line -> new User(Integer.parseInt(line.get(0)), userName, line.get(2), Integer.parseInt(line.get(3)), true))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such user found"));
    }

    @Override
    public void clear() {
        try {
            File f = new File(FILE_PATH);
            if (f.delete()) {
                System.out.println(f.getName() + " deleted");
            } else {
                System.out.println("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isNameAvailable(String username) {
        if (!Files.exists(Paths.get(FILE_PATH))) return true;

        EasyFile.importFromCSV(FILE_PATH).stream()
                .filter(line -> line.get(1).equals(username))
                .map(line -> false)
                .findFirst()
                .orElse(true);

        return false;
    }
}