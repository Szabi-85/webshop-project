package webshop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ProductCategory extends BaseModel{
    private String name;
    private String description;
    private String department;

    public ProductCategory(Integer id, String name, String description, String department){
        this.id = id;
        this.name = name;
        this.description = description;
        this.department = department;
    }

    public String toString(){
        return String.format("%s={id: %d, name: %s, department: %s, description: %s}",
                getClass().getSimpleName(), id, name, department, description
        );
    }
}