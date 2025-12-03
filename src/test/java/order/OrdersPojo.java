package order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class OrdersPojo {
    public String firstName;
    public String lastName;
    public String address;
    public int metroStation;
    public String phone;
    public int rentTime;
    public String deliveryDate;
    public String comment;
    public ArrayList<String> color;


    public static OrdersPojo getOrder(ArrayList<String> color){
        return new OrdersPojo("Naruto","Uchiha","Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha",color);
    }

}
