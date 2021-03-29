package webshop.dao.implementation.file;

import webshop.dao.CartDao;
import webshop.model.Cart;
import webshop.model.exception.DataNotFoundException;
import org.json.JSONArray;

import java.util.List;
import java.util.stream.Collectors;

public class CartDaoJsonFile implements CartDao {
    private static String FILE_PATH = "test/cart.json";


    @Override
    public void add(Cart cart) {
        JSONArray jsonArray = EasyFile.importFromJSON(FILE_PATH);
        int id;
        if (jsonArray.length() == 0) {
            id = 1;
        } else {
            id = jsonArray.getJSONArray(jsonArray.length() - 1).getInt(0) + 1;
        }
        cart.setId(id);
        JSONArray newCart = new JSONArray();
        newCart.put(cart.getId());
        newCart.put(cart.getCurrency().getCurrencyCode());
        jsonArray.put(newCart);
        EasyFile.exportToJSON(FILE_PATH, jsonArray);
    }

    @Override
    public Cart find(int id) {
        return EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .filter(jsonArray -> jsonArray.getInt(0) == id)
                .map(jsonArray -> new Cart(jsonArray.getInt(0), jsonArray.getString(1)))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such cart found!"));
    }

    @Override
    public void remove(int id) {
        EasyFile.exportToJSON(FILE_PATH, new JSONArray(EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .filter(list -> list.getInt(0) != id)
                .collect(Collectors.toList())
        ));
    }

    @Override
    public void clear() {
        EasyFile.exportToJSON(FILE_PATH, new JSONArray());
    }

    @Override
    public List<Cart> getAll() {
        return EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .map(cart -> new Cart(cart.getInt(0), cart.getString(1)))
                .collect(Collectors.toList());
    }
}