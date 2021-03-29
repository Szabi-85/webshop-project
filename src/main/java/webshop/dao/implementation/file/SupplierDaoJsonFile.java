package webshop.dao.implementation.file;

import webshop.dao.SupplierDao;
import webshop.model.Supplier;
import webshop.model.exception.DataNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class SupplierDaoJsonFile implements SupplierDao {
    private List<Supplier> suppliers;
    private File file = new File("test/supplier.json");

    private void initialize() {
        String json = "";
        try {
            if (!file.exists())
                file.createNewFile();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            json = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Type listType = new TypeToken<ArrayList<Supplier>>() {
        }.getType();
        suppliers = new Gson().fromJson(json, listType);
        if (suppliers == null)
            suppliers = new ArrayList<>();
    }

    private void saveToFile(String json) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Supplier supplier) {
        initialize();
        if (suppliers.size() > 0) {
            int id = Collections.max(suppliers, Comparator.comparing(a -> a.getId())).getId();
            supplier.setId(id + 1);
        } else
            supplier.setId(1);
        suppliers.add(supplier);
        String json = new Gson().toJson(suppliers);
        saveToFile(json);
    }

    @Override
    public Supplier find(int id) {
        initialize();
        return suppliers
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such supplier"));
    }

    @Override
    public void remove(int id) {
        initialize();
        suppliers.removeIf(a -> a.getId() == id);
        String json = new Gson().toJson(suppliers);
        saveToFile(json);
    }

    @Override
    public void clear() {
        initialize();
        suppliers.clear();
        String json = new Gson().toJson(suppliers);
        saveToFile(json);
    }

    @Override
    public List<Supplier> getAll() {
        initialize();
        return suppliers;
    }
}