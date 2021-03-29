package webshop.dao.implementation.db;

import webshop.dao.CartDao;
import webshop.dao.DataManager;
import webshop.dao.UserDao;
import webshop.model.Cart;
import webshop.model.User;
import webshop.model.exception.DataNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

public class UserDaoDb implements UserDao {

    private static String TABLE_NAME = "users";

    public UserDaoDb(){
        createTable();
    }

    public void createTable() {
        String SQL = String.format("CREATE TABLE IF NOT EXISTS %s ( id SERIAL, name TEXT, password TEXT, cartId INTEGER);" , TABLE_NAME);
        EasyDb.getInstance().executeUpdate(SQL);
    }

    @Override
    public void add( User user ) {
        if (isNameAvailable(user.getName())) {
            CartDao cartDao = DataManager.getCartDao();
            Cart cart = new Cart();
            cartDao.add(cart);

            user.setCartId(cart.getId());

            String SQL = String.format("INSERT INTO %s (name, password, cartId) VALUES (?,?,?)", TABLE_NAME);
            int id = EasyDb.getInstance().executeUpdate( SQL, user.getName(), user.getPassword(), user.getCartId());
            user.setId(id);
        }
    }

    @Override
    public User find( String userName, String password ) {
        String SQL = String.format("SELECT * FROM %s WHERE name = ?", TABLE_NAME);
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(SQL, userName);

        User user = null;
        while (resultSet.next()) {
            user = new User(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    true);

            if (BCrypt.checkpw(password, user.getPassword())) return user;
        }

        throw new DataNotFoundException("No such user");

    }

    @Override
    public void clear() {
        EasyDb.getInstance().executeUpdate(String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME));
        createTable();
    }

    @Override
    public boolean isNameAvailable( String username ) {
        String SQL = String.format("SELECT name FROM %s WHERE name = ?", TABLE_NAME);
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(SQL, username);

        while (resultSet.next()) {
            if(resultSet.getString("name").equals(username)) return false;  /* itt maraud */
        }

        return true;
    }
}