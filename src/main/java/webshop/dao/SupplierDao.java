package webshop.dao;

import webshop.model.Supplier;

import java.util.List;

public interface SupplierDao {
    void add(Supplier supplier);
    Supplier find(int id);
    void remove(int id);
    void clear();
    List<Supplier> getAll();
}
