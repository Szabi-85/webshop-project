package webshop.dao.implementation.file;

import webshop.dao.DataManager;
import webshop.dao.ProductDao;
import webshop.model.Product;
import webshop.model.ProductCategory;
import webshop.model.Supplier;
import webshop.model.exception.DataNotFoundException;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ProductDaoJsonFile implements ProductDao {
    private static final String FILE_PATH = "test/products.json";
    @Override
    public void add(Product product) {
        JSONArray products = EasyFile.importFromJSON(FILE_PATH);
        if (products.length() == 0) {
            product.setId(1);
            JSONArray newProduct = new JSONArray();
            newProduct.put(product.getId());
            newProduct.put(product.getName());
            newProduct.put(product.getDescription());
            newProduct.put(product.getDefaultPrice());
            newProduct.put(product.getDefaultCurrency().getCurrencyCode());
            newProduct.put(product.getProductCategory().getId());
            newProduct.put(product.getSupplier().getId());
            products.put(newProduct);
        } else {
            JSONArray lastElement = products.getJSONArray(products.length() - 1);
            int lastId = lastElement.getInt(0);
            int newId = lastId + 1;
            product.setId(newId);
            JSONArray newProduct = new JSONArray();
            newProduct.put(product.getId());
            newProduct.put(product.getName());
            newProduct.put(product.getDescription());
            newProduct.put(product.getDefaultPrice());
            newProduct.put(product.getDefaultCurrency().getCurrencyCode());
            newProduct.put(product.getProductCategory().getId());
            newProduct.put(product.getSupplier().getId());
            products.put(newProduct);
        }
        EasyFile.exportToJSON(FILE_PATH, products);
    }

    @Override
    public Product find(int id) {
        JSONArray products = EasyFile.importFromJSON(FILE_PATH);
        for (int i = 0; i < products.length(); i++) {
            JSONArray element = products.getJSONArray(i);
            if (element.getInt(0) == id) {
                return new Product(
                        element.getInt(0),
                        element.getString(1),
                        element.getBigDecimal(3),
                        element.getString(4),
                        element.getString(2),
                        DataManager.getProductCategoryDao().find(element.getInt(5)),
                        DataManager.getSupplierDao().find(element.getInt(6))
                );
            }
        }
        throw new DataNotFoundException("No such product.");
    }

    @Override
    public void remove(int id) {
        JSONArray products = EasyFile.importFromJSON(FILE_PATH);
        JSONArray newProducts = new JSONArray();
        for (int i = 0; i < products.length(); i++) {
            JSONArray element = products.getJSONArray(i);
            if (element.getInt(0) != id) {
                newProducts.put(element);
            }
        }
        EasyFile.exportToJSON(FILE_PATH, newProducts);
    }

    @Override
    public void clear() {
        EasyFile.exportToJSON(FILE_PATH, new JSONArray());
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        JSONArray bySupplier = EasyFile.importFromJSON(FILE_PATH);
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < bySupplier.length(); i++) {
            JSONArray product = bySupplier.getJSONArray(i);
            if (product.getInt(6) == supplier.getId()) {
                products.add(new Product(
                        product.getInt(0),
                        product.getString(1),
                        product.getBigDecimal(3),
                        product.getString(5),
                        product.getString(4),
                        DataManager.getProductCategoryDao().find(product.getInt(5)),
                        DataManager.getSupplierDao().find(product.getInt(6)))
                );
            }
        }
        return products;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        JSONArray allProducts = EasyFile.importFromJSON(FILE_PATH);
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < allProducts.length(); i++) {
            JSONArray product = allProducts.getJSONArray(i);
            if (productCategory.getId() == product.getInt(5)) {
                productList.add(new Product(
                        product.getInt(0),
                        product.getString(1),
                        product.getBigDecimal(3),
                        product.getString(4),
                        product.getString(2),
                        DataManager.getProductCategoryDao().find(product.getInt(5)),
                        DataManager.getSupplierDao().find(product.getInt(6)))
                );
            }
        }
        return productList;
    }

    @Override
    public List<Product> getAll() {
        JSONArray allProducts = EasyFile.importFromJSON(FILE_PATH);
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < allProducts.length(); i++) {
            JSONArray product = allProducts.getJSONArray(i);
            productList.add(new Product(
                    product.getInt(0),
                    product.getString(1),
                    product.getBigDecimal(3),
                    product.getString(5),
                    product.getString(4),
                    DataManager.getProductCategoryDao().find(product.getInt(5)),
                    DataManager.getSupplierDao().find(product.getInt(6)))
            );
        }
        return productList;
    }
}