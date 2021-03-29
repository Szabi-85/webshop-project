package webshop.dao.implementation.mem;

import webshop.model.exception.DataNotFoundException;
import webshop.dao.ProductCategoryDao;
import webshop.model.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoMem implements ProductCategoryDao {

    private List<ProductCategory> data = new ArrayList<>();

    @Override
    public void add(ProductCategory category) {
        category.setId(data.isEmpty() ? 1 : data.get(data.size() - 1).getId() + 1);
        data.add(category);
    }

    @Override
    public ProductCategory find(int id) {
        return data
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such category"));
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public List<ProductCategory> getAll() {
        return data;
    }
}