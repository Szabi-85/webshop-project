package webshop.dao.implementation.file;

import webshop.dao.CartDao;
import webshop.dao.DataManager;
import webshop.dao.UserDao;
import webshop.model.Cart;
import webshop.model.User;
import webshop.model.exception.DataNotFoundException;
import org.json.JSONArray;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class UserDaoJsonFile implements UserDao {
    private static String FILE_PATH = "test/users.json";

    @Override
    public void add(User user) {

        if (isNameAvailable(user.getName())) {
            CartDao cartDao = DataManager.getCartDao();
            Cart cart = new Cart();
            cartDao.add(cart);

            user.setCartId(cart.getId());

            try {
                if (!Files.exists(Paths.get(FILE_PATH))) {
                    Files.createFile(Paths.get(FILE_PATH));
                }

                List<String> list = Files.lines(Paths.get(FILE_PATH)).collect(Collectors.toList());

                if (list.size() == 0) {
                    list.add("[]");
                }

                JSONArray jsonArray = new JSONArray(list.get(0));

                if (jsonArray.length() == 0) {
                    user.setId(1);
                    JSONArray newItem = new JSONArray();
                    newItem.put(user.getId());
                    newItem.put(user.getName());
                    newItem.put(user.getPassword());
                    newItem.put(user.getCartId());
                    jsonArray.put(newItem);
                } else {
                    JSONArray lastItem = jsonArray.getJSONArray(jsonArray.length() - 1);
                    int newId = lastItem.getInt(0) + 1;
                    user.setId(newId);
                    JSONArray newItem = new JSONArray();
                    newItem.put(user.getId());
                    newItem.put(user.getName());
                    newItem.put(user.getPassword());
                    newItem.put(user.getCartId());
                    jsonArray.put(newItem);
                }
                String result = jsonArray.toString();
                Files.write(Paths.get(FILE_PATH), result.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User find(String userName, String password) {

        if (!Files.exists(Paths.get(FILE_PATH)))
            throw new DataNotFoundException("No such user found!");

        try {
            JSONArray jsonArray = new JSONArray(Files.lines(Paths.get(FILE_PATH)).collect(Collectors.toList())
                    .stream().findFirst().orElse("[]"));

            return jsonArray.toList().stream()
                    .map(String::valueOf)
                    .map(JSONArray::new)
                    .filter(listItem -> listItem.getString(1).equals(userName) && BCrypt.checkpw(password, listItem.getString(2)))
                    .map(item -> new User(item.getInt(0), userName, item.getString(2), item.getInt(3), true))
                    .findFirst()
                    .orElseThrow(() -> new DataNotFoundException("No such user found!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

        JSONArray data = EasyFile.importFromJSON(FILE_PATH);

        for (int i = 0; i < data.length(); i++) {
            JSONArray user = data.getJSONArray(i);
            if (user.getString(1).equals(username)) return false;
        }
        return true;
    }
}