package webshop.dao.implementation.file;

import webshop.dao.DataManager;
import webshop.dao.LineItemDao;
import webshop.model.Cart;
import webshop.model.LineItem;
import webshop.model.Product;
import webshop.model.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LineItemDaoCsvFile implements LineItemDao {
    private static String FILE_PATH = "test/line_item.csv";

    @Override
    public void add(LineItem lineItem) {
        List<List<String>> data = EasyFile.importFromCSV(FILE_PATH);
        lineItem.setId(data.size() == 0 ? 1 : (Integer.parseInt(data.get(data.size() - 1).get(0)) + 1));
        data.add(Arrays.asList(
                String.valueOf(lineItem.getId()),
                String.valueOf(lineItem.getProduct().getId()),
                String.valueOf(lineItem.getCartId()),
                String.valueOf(lineItem.getQuantity())));
        EasyFile.exportToCSV(FILE_PATH, data);
    }

    @Override
    public void remove(LineItem lineItem) {
        EasyFile.exportToCSV(FILE_PATH, EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .filter(list -> Integer.parseInt(list.get(0)) != lineItem.getId())
                .collect(Collectors.toList())
        );
    }

    @Override
    public void clear() {
        EasyFile.exportToCSV(FILE_PATH, new ArrayList<>());
    }

    @Override
    public void update(LineItem lineItem, int quantity) {
        EasyFile.exportToCSV(FILE_PATH, EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .map(item -> {
                    if (lineItem.getId() == Integer.parseInt(item.get(0))) item.set(3, String.valueOf(quantity));
                    return item;
                }).collect(Collectors.toList()));
    }

    @Override
    public LineItem find(int id) {
        return EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .filter(item -> Integer.parseInt(item.get(0)) == id)
                .map(item -> new LineItem(
                        id,
                        DataManager.getProductDao().find(Integer.parseInt(item.get(1))),
                        Integer.parseInt(item.get(2)),
                        Integer.parseInt(item.get(3))))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such line-item found!"));
    }

    @Override
    public List<LineItem> getBy(Cart cart) {
        Product product = DataManager.getProductDao().find(1);
        return EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .filter(item -> Integer.parseInt(item.get(2)) == cart.getId())
                .map(item -> new LineItem(
                        Integer.parseInt(item.get(0)),
                        DataManager.getProductDao().find(Integer.parseInt(item.get(1))),
                        Integer.parseInt(item.get(2)),
                        Integer.parseInt(item.get(3))))
                .collect(Collectors.toList());
    }
}