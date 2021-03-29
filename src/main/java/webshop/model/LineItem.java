package webshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class LineItem extends BaseModel {

    @Getter private final Product product;
    @Getter private final int cartId;
    @Getter @Setter private int quantity;

    public LineItem(int id, Product product, int cartId, int quantity) {
        this.id = id;
        this.product = product;
        this.cartId = cartId;
        this.quantity = quantity;
    }

    public String toString() {
        return String.format("%1$s={quantity: %2$d, product: %3$s}",
                getClass().getSimpleName(),
                quantity,
                product
        );
    }
}