package webshop.dao;

import webshop.dao.implementation.db.*;
import webshop.dao.implementation.file.*;
import webshop.dao.implementation.mem.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DaoImplementationSupplier {
    private final ProductDao productDao;
    private final ProductCategoryDao productCategoryDao;
    private final SupplierDao supplierDao;
    private final CartDao cartDao;
    private final LineItemDao lineItemDao;
    private final UserDao userDao;

    public static DaoImplementationSupplier getInstance(DaoType daoType) {
        printImplementation(daoType);
        switch (daoType) {
            case CSV:
                return new DaoImplementationSupplier(
                        new ProductDaoCsvFile(),
                        new ProductCategoryDaoCsvFile(),
                        new SupplierDaoCsvFile(),
                        new CartDaoCsvFile(),
                        new LineItemDaoCsvFile(),
                        new UserDaoCsvFile());
            case JSON:
                return new DaoImplementationSupplier(
                        new ProductDaoJsonFile(),
                        new ProductCategoryDaoJsonFile(),
                        new SupplierDaoJsonFile(),
                        new CartDaoJsonFile(),
                        new LineItemDaoJsonFile(),
                        new UserDaoJsonFile());
            case DATABASE:
                return new DaoImplementationSupplier(
                        new ProductDaoDb(),
                        new ProductCategoryDaoDb(),
                        new SupplierDaoDb(),
                        new CartDaoDb(),
                        new LineItemDaoDb(),
                        new UserDaoDb());
            case MEMORY:
            default:
                return new DaoImplementationSupplier(
                        new ProductDaoMem(),
                        new ProductCategoryDaoMem(),
                        new SupplierDaoMem(),
                        new CartDaoMem(),
                        new LineItemDaoMem(),
                        new UserDaoMem());
        }
    }

    private static void printImplementation(DaoType daoType) {
        System.out.printf("--- Using %s DAO implementations ---\n", daoType);
    }
}