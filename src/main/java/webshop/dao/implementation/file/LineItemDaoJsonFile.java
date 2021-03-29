package webshop.dao.implementation.file;

import webshop.dao.DataManager;
import webshop.dao.LineItemDao;
import webshop.model.Cart;
import webshop.model.LineItem;
import webshop.model.exception.DataNotFoundException;
import org.json.JSONArray;

import java.util.List;
import java.util.stream.Collectors;

public class LineItemDaoJsonFile implements LineItemDao {
    private static String FILE_PATH = "test/line_item.json";

    @Override
    public void add(LineItem lineItem) {
        JSONArray data = EasyFile.importFromJSON(FILE_PATH);
        lineItem.setId(data.length() == 0 ? 1 : (((JSONArray) data.get(data.length() - 1)).getInt(0)) + 1);
        data.put(new JSONArray() {
            {
                this.put(lineItem.getId());
                this.put(lineItem.getProduct().getId());
                this.put(lineItem.getCartId());
                this.put(lineItem.getQuantity());
            }});
        System.out.println(data);
        EasyFile.exportToJSON(FILE_PATH, data);
    }

    @Override
    public void remove(LineItem lineItem) {
        EasyFile.exportToJSON(FILE_PATH, new JSONArray(EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .filter(item -> item.getInt(0) != lineItem.getId())
                .collect(Collectors.toList()))
        );
    }

    @Override
    public void clear() {
        EasyFile.exportToJSON(FILE_PATH, new JSONArray());
    }

    @Override
    public void update(LineItem lineItem, int quantity) {
        EasyFile.exportToJSON(FILE_PATH, new JSONArray(EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(item -> (JSONArray) item)
                .map(item -> {
                    if (lineItem.getId() == item.getInt(0)) item.put(3, quantity);
                    return item;
                }).collect(Collectors.toList())));
    }

    @Override
    public LineItem find(int id) {
        return EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .filter(item -> item.getInt(0) == id)
                .map(jsonArray -> new LineItem(
                        jsonArray.getInt(0),
                        DataManager.getProductDao().find(jsonArray.getInt(1)),
                        jsonArray.getInt(2),
                        jsonArray.getInt(3)))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such line-item found!"));
    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        return EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .filter(jsonArray -> jsonArray.getInt(2) == cart.getId())
                .map(jsonArray -> new LineItem(
                        jsonArray.getInt(0),
                        DataManager.getProductDao().find(jsonArray.getInt(1)),
                        jsonArray.getInt(2),
                        jsonArray.getInt(3)))
                .collect(Collectors.toList());
    }
}