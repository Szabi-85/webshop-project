package webshop.dao.implementation.db;

import webshop.dao.DataManager;
import webshop.dao.SupplierDao;
import webshop.model.Supplier;
import webshop.model.exception.DataNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoDb implements SupplierDao {
    private static String tableName = "supplier";

    public SupplierDaoDb() {
        initialize();
    }

    private void initialize() {
        String SQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (id SERIAL, name TEXT, description TEXT, PRIMARY KEY (id));";
        try (Connection connection = EasyDb.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Supplier supplier) {
        String SQL = "INSERT INTO " + tableName + " (name,description) VALUES (?,?);";
        try (Connection connection = EasyDb.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getDescription());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            supplier.setId(resultSet.getInt("id"));
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Supplier find(int id) {
        String SQL = "SELECT * FROM " + tableName + " WHERE id=?;";
        Supplier supplier = null;
        try (Connection connection = EasyDb.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                supplier.setId(id);
            }
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(supplier==null)
        throw new DataNotFoundException("No such supplier");
        return supplier;
    }

    @Override
    public void remove(int id) {
        String SQL = "DELETE FROM " + tableName + " WHERE id=?;";
        try (Connection connection = EasyDb.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clear() {
        String SQL = "DROP TABLE IF EXISTS " + tableName + ";";
        try (Connection connection = EasyDb.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        initialize();
    }

    @Override
    public List<Supplier> getAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String SQL = "SELECT * FROM " + tableName + ";";
        try (Connection connection = EasyDb.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                supplier.setId(resultSet.getInt("id"));
                suppliers.add(supplier);
            }
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return suppliers;
    }


}
