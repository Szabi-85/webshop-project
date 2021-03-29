package webshop.dao.implementation.db;

import webshop.dao.ProductCategoryDao;
import webshop.model.ProductCategory;
import webshop.model.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoDb implements ProductCategoryDao{
    private static String TABLE_NAME = "product_category";

    public ProductCategoryDaoDb(){
        createTable();
    }

    public void createTable(){
        String SQL = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL, name TEXT, description TEXT, department TEXT, PRIMARY KEY(id));", TABLE_NAME);
        EasyDb.getInstance().executeUpdate(SQL);
    }

    public void resetTable(){
        EasyDb.getInstance().executeUpdate(String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME));
    }

    @Override
    public void add(ProductCategory category){
        String SQL = String.format("INSERT INTO %s (name, description, department) VALUES (?, ?, ?);", TABLE_NAME);
        Integer id = EasyDb.getInstance().executeUpdate(SQL, category.getName(), category.getDescription(), category.getDepartment());
        category.setId(id);
    }

    @Override
    public ProductCategory find(int id){
        String SQL = String.format("SELECT * FROM %s WHERE id = ?;", TABLE_NAME);
        EasyResultSet easyResultSet = EasyDb.getInstance().executeQuery(SQL, id);

        while (easyResultSet.next()){
            return new ProductCategory(
                    easyResultSet.getInt(1),
                    easyResultSet.getString(2),
                    easyResultSet.getString(3),
                    easyResultSet.getString(4));
        }

        throw new DataNotFoundException("No such category");
    }

    @Override
    public void remove(int id){
        String SQL = String.format("DELETE FROM %s WHERE id = ?;", TABLE_NAME);
        EasyDb.getInstance().executeUpdate(SQL, id);
    }

    @Override
    public void clear(){
        resetTable();
        createTable();
    }

    @Override
    public List<ProductCategory> getAll(){
        String SQL = String.format("SELECT * FROM %s", TABLE_NAME);
        EasyResultSet easyResultSet = EasyDb.getInstance().executeQuery(SQL);

        List<ProductCategory> data = new ArrayList<>();
        while (easyResultSet.next()){
            data.add(new ProductCategory(
                    easyResultSet.getInt(1),
                    easyResultSet.getString(2),
                    easyResultSet.getString(3),
                    easyResultSet.getString(4)));
        }

        return data;
    }
}