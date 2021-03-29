package webshop.dao.implementation.db;

import webshop.dao.CartDao;
import webshop.model.Cart;
import webshop.model.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CartDaoDb implements CartDao {
    private static String TABLE_NAME = "cart";

    public CartDaoDb(){
        createTable();
    }

    private void createTable(){
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL, currency TEXT);", TABLE_NAME);
        EasyDb.getInstance().executeUpdate(sql);
    }

    @Override
    public void add(Cart cart) {
        String sql = String.format("INSERT INTO %s (currency) VALUES (?);", TABLE_NAME);
        int id = EasyDb.getInstance().executeUpdate(sql, cart.getCurrency().getCurrencyCode());
        cart.setId(id);
    }

    @Override
    public Cart find(int id) {
        String sql = String.format("SELECT * FROM %s WHERE id = ?;", TABLE_NAME);
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(sql, id);

        while (resultSet.next()) {
            return new Cart(
                    resultSet.getInt(1),
                    resultSet.getString(2));
        }

        throw new DataNotFoundException("No such cart found!");
    }

    @Override
    public void remove(int id) {
        String sql = String.format("DELETE FROM %s WHERE id = ?;", TABLE_NAME);
        EasyDb.getInstance().executeUpdate(sql, id);
    }

    @Override
    public void clear() {
        String sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
        EasyDb.getInstance().executeUpdate(sql);
        createTable();
    }

    @Override
    public List<Cart> getAll() {
        String sql = String.format("SELECT * FROM %s;", TABLE_NAME);
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(sql);
        List<Cart> carts = new ArrayList();
        while (resultSet.next()) {
            Cart cart = new Cart(resultSet.getString(2));
            cart.setId(resultSet.getInt(1));
            carts.add(cart);
        }
        return carts;
    }
}