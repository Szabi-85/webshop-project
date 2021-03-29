package webshop.dao.implementation.file;

import webshop.dao.CartDao;
import webshop.model.Cart;
import webshop.model.exception.DataNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartDaoCsvFile implements CartDao {
    private static String FILE_PATH = "test/cart.csv";

    @Override
    public void add(Cart cart) {
        try {
            if (Files.notExists(Paths.get(FILE_PATH))) {
                Files.createFile(Paths.get(FILE_PATH));
            }
            List<String> list = Files.lines(Paths.get(FILE_PATH)).collect(Collectors.toList());
            if (list.isEmpty()) {
                list.add("1," + cart.getCurrency().getCurrencyCode());
            } else {
                String[] values = list.get(list.size() - 1).split(",");
                int id = Integer.parseInt(values[0]) + 1;
                list.add("" + id + "," + cart.getCurrency().getCurrencyCode());
            }
            String result = list.stream().collect(Collectors.joining("\n"));
            Files.write(Paths.get(FILE_PATH), result.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cart find(int id) {
        return EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .filter(list -> Integer.parseInt(list.get(0)) == id)
                .map(item -> new Cart(Integer.parseInt(item.get(0)), item.get(1)))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such cart found!"));
    }

    @Override
    public void remove(int id) {
        EasyFile.exportToCSV(FILE_PATH,
                EasyFile.importFromCSV(FILE_PATH)
                        .stream()
                        .filter(item -> Integer.parseInt(item.get(0)) != id)
                        .collect(Collectors.toList()));
    }

    @Override
    public void clear() {
        EasyFile.exportToCSV(FILE_PATH, new ArrayList<>());
    }

    @Override
    public List<Cart> getAll() {
        return EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .map(cart -> new Cart(Integer.parseInt(cart.get(0)), cart.get(1))).collect(Collectors.toList());
    }
}