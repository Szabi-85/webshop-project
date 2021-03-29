package webshop.dao.implementation.file;

import webshop.dao.ProductCategoryDao;
import webshop.model.ProductCategory;
import webshop.model.exception.DataNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryDaoCsvFile implements ProductCategoryDao{
    private static String FILE_PATH = "test/product_category.csv";

    @Override
    public void add(ProductCategory category){
        List<List<String>> data = EasyFile.importFromCSV(FILE_PATH);

        category.setId(data.size() == 0 ? 1 : (Integer.parseInt(data.get(data.size() - 1).get(0)) + 1));
        data.add(Arrays.asList(
                String.valueOf(category.getId()),
                category.getName(),
                category.getDescription(),
                category.getDepartment())
        );

        EasyFile.exportToCSV(FILE_PATH, data);
    }

    @Override
    public ProductCategory find(int id){
        return EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .filter(list -> Integer.parseInt(list.get(0)) == id)
                .map(category -> new ProductCategory(
                        Integer.parseInt(category.get(0)),
                        category.get(1),
                        category.get(2),
                        category.get(3)))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such category"));
    }

    @Override
    public void remove(int id){
        EasyFile.exportToCSV(FILE_PATH, EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .filter(list -> Integer.parseInt(list.get(0)) != id)
                .collect(Collectors.toList())
        );
    }

    @Override
    public void clear(){
        EasyFile.exportToCSV(FILE_PATH, new ArrayList<>());
    }

    @Override
    public List<ProductCategory> getAll(){
        return EasyFile.importFromCSV(FILE_PATH)
                .stream()
                .map(category -> new ProductCategory(
                        Integer.parseInt(category.get(0)),
                        category.get(1),
                        category.get(2),
                        category.get(3)))
                .collect(Collectors.toList());
    }
}