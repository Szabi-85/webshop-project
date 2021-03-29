package webshop.dao.implementation.mem;

import webshop.dao.CartDao;
import webshop.model.exception.DataNotFoundException;
import webshop.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDaoMem implements CartDao {

    private List<Cart> data = new ArrayList<>();

    @Override
    public void add(Cart cart) {
        if (data.isEmpty()) {
            cart.setId(1);
        } else {
            Cart lastCart = data.get(data.size() - 1);
            cart.setId(lastCart.getId() + 1);
        }
        data.add(cart);
    }

    @Override
    public Cart find(int id) {
        return data
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such cart found!"));
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public void clear() {
        data = new ArrayList<>();
    }

    @Override
    public List<Cart> getAll() {
        return data;
    }
}