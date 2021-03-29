package webshop.dao;

import webshop.model.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    void add(ProductCategory category);
    ProductCategory find(int id);
    void remove(int id);
    void clear();
    List<ProductCategory> getAll();
}
