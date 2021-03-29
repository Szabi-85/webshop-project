package webshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ShippingInfo {

    private String name;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;

    public String toString() {
        return String.format("%1$s={" +
                        "name: %2$s, " +
                        "email: %3$s, " +
                        "phoneNumber: %4$s, " +
                        "billingAddress: %5$s, " +
                        "shippingAddress: %6$s}",
                getClass().getSimpleName(),
                name,
                email,
                phoneNumber,
                billingAddress,
                shippingAddress
        );
    }
}