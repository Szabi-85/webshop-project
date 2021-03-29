package webshop.dao.implementation.file;

import webshop.dao.SupplierDao;
import webshop.model.Supplier;
import webshop.model.exception.DataNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SupplierDaoCsvFile implements SupplierDao {
    private List<Supplier> suppliers;
    private final File file = new File("test/supplier.csv");

    private void initialize() {
        suppliers = new ArrayList<>();
        if (file.exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    String[] idNameDescription = line.split(",");
                    Supplier supplier = new Supplier(idNameDescription[1], idNameDescription[2]);
                    supplier.setId(Integer.parseInt(idNameDescription[0]));
                    suppliers.add(supplier);
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToCSV() {
        try {
            file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            for (Supplier supplier : suppliers) {
                fileWriter.write(String.valueOf(supplier.getId()));
                fileWriter.write(",");
                fileWriter.write(supplier.getName());
                fileWriter.write(",");
                fileWriter.write(supplier.getDescription());
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.close();
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
        saveToCSV();
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
        saveToCSV();
    }

    @Override
    public void clear() {
        initialize();
        suppliers.clear();
        saveToCSV();
    }

    @Override
    public List<Supplier> getAll() {
        initialize();
        return suppliers;
    }
}