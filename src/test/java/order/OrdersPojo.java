package order;

import java.util.ArrayList;

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

    public OrdersPojo(String address, ArrayList<String> color, String comment, String deliveryDate, String firstName, String lastName, int metroStation, String phone, int rentTime) {
        this.address = address;
        this.color = color;
        this.comment = comment;
        this.deliveryDate = deliveryDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
    }
    public static OrdersPojo getOrder(ArrayList<String> color){
        return new OrdersPojo("Konoha, 142 apt.", color,"Saske, come back to Konoha","2020-06-06","Naruto","Uchiha",4,"+7 800 355 35 35",5);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMetroStation(int metroStation) {
        this.metroStation = metroStation;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setColor(ArrayList<String> color) {
        this.color = color;
    }

    public int getRentTime() {
        return rentTime;
    }

    public String getPhone() {
        return phone;
    }

    public int getMetroStation() {
        return metroStation;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public ArrayList<String> getColor() {
        return color;
    }

    public String getAddress() {
        return address;
    }
}
