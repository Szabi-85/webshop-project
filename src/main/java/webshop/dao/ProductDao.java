package webshop.dao;

import webshop.model.Supplier;
import webshop.model.Product;
import webshop.model.ProductCategory;

import java.util.List;

public interface ProductDao {
    void add(Product product);
    Product find(int id);
    void remove(int id);
    void clear();
    List<Product> getBy(Supplier supplier);
    List<Product> getBy(ProductCategory productCategory);
    List<Product> getAll();
}
