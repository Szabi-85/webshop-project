package webshop.dao;

import webshop.model.*;
import webshop.util.BaseData;

public class DataManager {
    private static final DaoType DAO_TYPE;

    static {
        switch (System.getenv("DAO_TYPE")){
            case "database":
                DAO_TYPE = DaoType.DATABASE;
                break;
            case "json":
                DAO_TYPE = DaoType.JSON;
                break;
            case "csv":
                DAO_TYPE = DaoType.CSV;
                break;
            case "memory":
            default:
                DAO_TYPE = DaoType.MEMORY;
        }
    }

    private static DaoImplementationSupplier daoImplementationSupplier = DaoImplementationSupplier.getInstance(DAO_TYPE);

    public static void init() {
        initCart();
        initSuppliers();
        initProductCategories();
        initProducts();
        initUsers();
    }

    public static void clear() {
        daoImplementationSupplier.getProductDao().clear();
        daoImplementationSupplier.getProductCategoryDao().clear();
        daoImplementationSupplier.getSupplierDao().clear();
        daoImplementationSupplier.getCartDao().clear();
        daoImplementationSupplier.getLineItemDao().clear();
        daoImplementationSupplier.getUserDao().clear();
    }

    private static void initCart() {
        Cart cart = new Cart();
        daoImplementationSupplier.getCartDao().add(cart);
    }

    private static void initSuppliers() {
        for (Supplier supplier : BaseData.defaultSuppliers()) {
            daoImplementationSupplier.getSupplierDao().add(supplier);
        }
    }

    private static void initProductCategories() {
        for (ProductCategory productCategory : BaseData.defaultProductCategories()) {
            daoImplementationSupplier.getProductCategoryDao().add(productCategory);
        }
    }

    private static void initProducts() {
        for (Product product : BaseData.defaultProducts()) {
            daoImplementationSupplier.getProductDao().add(product);
        }
    }

    private static void initUsers() {
        for (User user : BaseData.defaultUsers()) {
            daoImplementationSupplier.getUserDao().add(user);
        }
    }

    public static ProductDao getProductDao(){
        return daoImplementationSupplier.getProductDao();
    }

    public static ProductCategoryDao getProductCategoryDao(){
        return daoImplementationSupplier.getProductCategoryDao();
    }

    public static SupplierDao getSupplierDao(){
        return daoImplementationSupplier.getSupplierDao();
    }

    public static CartDao getCartDao(){
        return daoImplementationSupplier.getCartDao();
    }

    public static LineItemDao getLineItemDao(){
        return daoImplementationSupplier.getLineItemDao();
    }

    public static UserDao getUserDao(){
        return daoImplementationSupplier.getUserDao();
    }
}