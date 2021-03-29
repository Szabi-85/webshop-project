package webshop.dao.implementation.db;

import webshop.dao.DataManager;
import webshop.dao.ProductDao;
import webshop.model.Product;
import webshop.model.ProductCategory;
import webshop.model.Supplier;
import webshop.model.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ProductDaoDb implements ProductDao {
    private static final String TABLE_NAME = "products";

    public ProductDaoDb(){
        createTable();
    }

    public void createTable() {
        String SQL = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL, name TEXT, description TEXT, defaultPrice DECIMAL, defaultCurrency TEXT, productCategory INTEGER, supplier INTEGER);", TABLE_NAME);
        EasyDb.getInstance().executeUpdate(SQL);
    }

    @Override
    public void add(Product product) {
        String SQL = String.format("INSERT INTO %s (name, description, defaultPrice, defaultCurrency, productCategory, supplier) VALUES (?, ?, ?, ?, ?, ?)", TABLE_NAME);
        int id = EasyDb.getInstance().executeUpdate(SQL,
                product.getName(),
                product.getDescription(),
                product.getDefaultPrice(),
                product.getDefaultCurrency().getCurrencyCode(),
                product.getProductCategory().getId(),
                product.getSupplier().getId());
        product.setId(id);
    }

    @Override
    public Product find(int id) {
        String SQL = String.format("SELECT * FROM %s WHERE id = ?;", TABLE_NAME);
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(SQL, id);

        Product product = null;
        while (resultSet.next()) {
            product = new Product(resultSet.getString(2),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    DataManager.getProductCategoryDao().find(resultSet.getInt(6)),
                    DataManager.getSupplierDao().find(resultSet.getInt(7))
            );
            product.setId(resultSet.getInt(1));
        }
        if (product == null) {
            throw new DataNotFoundException("No such product");
        }
        return product;
    }

    @Override
    public void remove(int id) {
        String SQL = "DELETE FROM " + TABLE_NAME + " WHERE id = ?;";
        EasyDb.getInstance().executeUpdate(SQL, id);
    }

    @Override
    public void clear() {
        String SQL = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        EasyDb.getInstance().executeUpdate(SQL);
        createTable();
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE supplier = ?;";
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(SQL, supplier.getId());

        List<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(resultSet.getString(2),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    DataManager.getProductCategoryDao().find(resultSet.getInt(6)),
                    DataManager.getSupplierDao().find(resultSet.getInt(7))
            );
            product.setId(resultSet.getInt(1));
            products.add(product);
        }
        return products;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE productCategory = ?;";
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(SQL, productCategory.getId());
        List<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(resultSet.getString(2),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    DataManager.getProductCategoryDao().find(resultSet.getInt(6)),
                    DataManager.getSupplierDao().find(resultSet.getInt(7))
            );
            product.setId(resultSet.getInt(1));
            products.add(product);
        }
        return products;
    }

    @Override
    public List<Product> getAll() {
        String SQL = "SELECT * FROM " + TABLE_NAME + ";";
        EasyResultSet resultSet = EasyDb.getInstance().executeQuery(SQL);
        List<Product> products = new ArrayList();
        while (resultSet.next()) {
            Product product = new Product(resultSet.getString(2),
                    resultSet.getBigDecimal(4),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    DataManager.getProductCategoryDao().find(resultSet.getInt(6)),
                    DataManager.getSupplierDao().find(resultSet.getInt(7))
            );
            product.setId(resultSet.getInt(1));
            products.add(product);
        }
        return products;
    }
}