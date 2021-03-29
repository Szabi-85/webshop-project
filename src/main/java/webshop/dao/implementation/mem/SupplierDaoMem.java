package webshop.dao.implementation.mem;

import webshop.model.exception.DataNotFoundException;
import webshop.dao.SupplierDao;
import webshop.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierDaoMem implements SupplierDao {

    private List<Supplier> data = new ArrayList<>();

    @Override
    public void add(Supplier supplier) {
        supplier.setId(data.isEmpty() ? 1 : data.get(data.size() - 1).getId() + 1);
        data.add(supplier);
    }

    @Override
    public Supplier find(int id) {
        return data
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such supplier"));
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
    public List<Supplier> getAll() {
        return data;
    }
}