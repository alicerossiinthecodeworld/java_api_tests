import com.github.javafaker.PhoneNumber;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    public final List<String> color;
    public final String address;
    public final String firstName;
    public final String lastName;
    public final Object deliveryDate;
    public final String metroStation;
    public final String phone;
    public final int rentTime;
    public final String comment;

    public Order(List<String> color, String address, String firstName, String lastName, String metroStation, String phone, int rentTime, String comment) {
        this.color = color;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.comment = comment;
        this.deliveryDate = LocalDateTime.now().toString();
    }
}
