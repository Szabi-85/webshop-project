package webshop.dao.implementation.db;

import webshop.dao.DataManager;
import webshop.dao.LineItemDao;
import webshop.model.Cart;
import webshop.model.LineItem;
import webshop.model.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class LineItemDaoDb implements LineItemDao {
    private static final String TABLE_NAME = "line_item";

    public LineItemDaoDb(){
        createTable();
    }

    public void createTable(){
        EasyDb.getInstance().executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL, product INTEGER, cart_id INTEGER, quantity INTEGER);",
                        TABLE_NAME));
    }

    @Override
    public void add(LineItem lineItem) {
        int id = EasyDb.getInstance().executeUpdate(
                String.format("INSERT INTO %s (product, cart_id, quantity) VALUES (?, ?, ?);", TABLE_NAME),
                lineItem.getProduct().getId(),
                lineItem.getCartId(),
                lineItem.getQuantity());
        lineItem.setId(id);
    }

    @Override
    public void remove(LineItem lineItem) {
        EasyDb.getInstance().executeUpdate(
                String.format("DELETE FROM %s WHERE id = ?;", TABLE_NAME),
                lineItem.getId());
    }

    @Override
    public void clear() {
        EasyDb.getInstance().executeUpdate(String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME));
        createTable();
    }

    @Override
    public void update(LineItem lineItem, int quantity) {
        EasyDb.getInstance().executeUpdate(
                String.format("UPDATE %s SET product = ?, cart_id = ?, quantity = ? WHERE id = ?;", TABLE_NAME),
                lineItem.getProduct().getId(),
                lineItem.getCartId(),
                quantity,
                lineItem.getId());
    }

    @Override
    public LineItem find(int id) {
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(
                String.format("SELECT * FROM %s WHERE id = ?;", TABLE_NAME), id);
        while (resultSet.next()){
            LineItem lineItem = new LineItem(DataManager.getProductDao().find(resultSet.getInt(2)),
                    resultSet.getInt(3),
                    resultSet.getInt(4));
            lineItem.setId(resultSet.getInt(1));
            return lineItem;
        }
        throw new DataNotFoundException("No such category");
    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(
                String.format("SELECT * FROM %s WHERE cart_id = ?", TABLE_NAME), cart.getId());
        List<LineItem> lineItems = new ArrayList<>();
        while (resultSet.next()){
            LineItem lineItem = new LineItem(DataManager.getProductDao().find(resultSet.getInt(2)),
                    resultSet.getInt(3),
                    resultSet.getInt(4));
            lineItem.setId(resultSet.getInt(1));
            lineItems.add(lineItem);
        }
        return lineItems;
    }
}