package webshop.dao.implementation.file;

import webshop.dao.DataManager;
import webshop.dao.ProductDao;
import webshop.model.Product;
import webshop.model.ProductCategory;
import webshop.model.Supplier;
import webshop.model.exception.DataNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDaoCsvFile implements ProductDao {
    @Override
    public void add(Product product) {
        List<List<String>> data = EasyFile.importFromCSV("test/products.csv");
        product.setId(data.size() == 0 ? 1 : Integer.parseInt(data.get(data.size() - 1).get(0)) + 1);
        data.add(Arrays.asList(
                String.valueOf(product.getId()),
                product.getName(),
                product.getDescription(),
                String.valueOf(product.getDefaultPrice()),
                product.getDefaultCurrency().getCurrencyCode(),
                String.valueOf(product.getProductCategory().getId()),
                String.valueOf(product.getSupplier().getId())
        ));
        EasyFile.exportToCSV("test/products.csv", data);
    }

    @Override
    public Product find(int id) {
        List<List<String>> products = EasyFile.importFromCSV("test/products.csv");
        for (int i = 0; i < products.size(); i++) {
            if (id == Integer.parseInt(products.get(i).get(0))) {
                return new Product(
                        Integer.parseInt(products.get(i).get(0)),
                        products.get(i).get(1),
                        new BigDecimal(products.get(i).get(3)),
                        products.get(i).get(4),
                        products.get(i).get(2),
                        DataManager.getProductCategoryDao().find(Integer.parseInt(products.get(i).get(5))),
                        DataManager.getSupplierDao().find(Integer.parseInt(products.get(i).get(6)))
                );
            }
        }
        throw new DataNotFoundException("No such product.");
    }

    @Override
    public void remove(int id) {
        List<List<String>> products = EasyFile.importFromCSV("test/products.csv");
        List<List<String>> newProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (id != Integer.parseInt(products.get(i).get(0))) {
                newProducts.add(products.get(i));
            }
        }
        EasyFile.exportToCSV("test/products.csv", newProducts);
    }

    @Override
    public void clear() {
        if (Files.exists(Paths.get("test/products.csv"))) {
            try {
                Files.delete(Paths.get("test/products.csv"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        List<List<String>> products = EasyFile.importFromCSV("test/products.csv");
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (supplier.getId() == Integer.parseInt(products.get(i).get(6))) {
                productList.add(new Product(
                        Integer.parseInt(products.get(i).get(0)),
                        products.get(i).get(1),
                        new BigDecimal(products.get(i).get(3)),
                        products.get(i).get(4),
                        products.get(i).get(2),
                        DataManager.getProductCategoryDao().find(Integer.parseInt(products.get(i).get(5))),
                        DataManager.getSupplierDao().find(Integer.parseInt(products.get(i).get(6)))
                ));
            }
        }
        return productList;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        List<List<String>> products = EasyFile.importFromCSV("test/products.csv");
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (productCategory.getId() == Integer.parseInt(products.get(i).get(5))) {
                productList.add(new Product(
                        Integer.parseInt(products.get(i).get(0)),
                        products.get(i).get(1),
                        new BigDecimal(products.get(i).get(3)),
                        products.get(i).get(4),
                        products.get(i).get(2),
                        DataManager.getProductCategoryDao().find(Integer.parseInt(products.get(i).get(5))),
                        DataManager.getSupplierDao().find(Integer.parseInt(products.get(i).get(6)))
                ));
            }
        }
        return productList;
    }

    @Override
    public List<Product> getAll() {
        List<List<String>> products = EasyFile.importFromCSV("test/products.csv");
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            productList.add(new Product(
                    Integer.parseInt(products.get(i).get(0)),
                    products.get(i).get(1),
                    new BigDecimal(products.get(i).get(3)),
                    products.get(i).get(4),
                    products.get(i).get(2),
                    DataManager.getProductCategoryDao().find(Integer.parseInt(products.get(i).get(5))),
                    DataManager.getSupplierDao().find(Integer.parseInt(products.get(i).get(6)))
            ));
        }
        return productList;
    }
}