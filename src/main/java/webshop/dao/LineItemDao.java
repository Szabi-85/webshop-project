package webshop.dao;

import webshop.model.Cart;
import webshop.model.LineItem;

import java.util.List;

public interface LineItemDao {
    void add(LineItem lineItem);
    void remove(LineItem lineItem);
    void clear();
    void update(LineItem lineItem, int quantity);
    LineItem find(int id);
    List<LineItem> getBy(Cart cart);
}
