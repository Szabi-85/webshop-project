package webshop.dao.implementation.file;

import webshop.dao.ProductCategoryDao;
import webshop.model.ProductCategory;
import webshop.model.exception.DataNotFoundException;

import org.json.JSONArray;
import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryDaoJsonFile implements ProductCategoryDao{
    private static String FILE_PATH = "test/product_category.json";

    @Override
    public void add(ProductCategory category){
        JSONArray data = EasyFile.importFromJSON(FILE_PATH);

        category.setId(data.length() == 0 ? 1 : (((JSONArray) data.get(data.length() - 1)).getInt(0)) + 1);
        data.put(new JSONArray(){{
            this.put(category.getId());
            this.put(category.getName());
            this.put(category.getDescription());
            this.put(category.getDepartment());
        }});

        EasyFile.exportToJSON(FILE_PATH, data);
    }

    @Override
    public ProductCategory find(int id){
        return EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .filter(jsonArray -> jsonArray.getInt(0) == id)
                .map(jsonArray -> new ProductCategory(
                        jsonArray.getInt(0),
                        jsonArray.getString(1),
                        jsonArray.getString(2),
                        jsonArray.getString(3)))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("No such category"));
    }

    @Override
    public void remove(int id){
        EasyFile.exportToJSON(FILE_PATH, new JSONArray(EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .filter(list -> list.getInt(0) != id)
                .collect(Collectors.toList())
        ));
    }

    @Override
    public void clear(){
        EasyFile.exportToJSON(FILE_PATH, new JSONArray());
    }

    @Override
    public List<ProductCategory> getAll(){
        return EasyFile.importFromJSON(FILE_PATH)
                .toList()
                .stream()
                .map(String::valueOf)
                .map(JSONArray::new)
                .map(jsonArray -> new ProductCategory(
                        jsonArray.getInt(0),
                        jsonArray.getString(1),
                        jsonArray.getString(2),
                        jsonArray.getString(3)))
                .collect(Collectors.toList());
    }
}