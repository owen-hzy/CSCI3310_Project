package csci3310.cuhk.edu.hk.project.dataModel;

/**
 * Created by csamyphew on 16/12/15.
 */
public class Record {
    int id;
    double price;
    String type; // Income or Expense
    String created_at;

    //constructors
    public Record(){

    }
    public Record(double price, String type) {
        this.price = price;
        this.type = type;
    }
    public Record(int id, double price, String type) {
        this.id = id;
        this.price = price;
        this.type = type;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setType (String type) {
        this.type = type;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    //getters
    public long getId() {
        return this.id;
    }
    public double getPrice() {
        return this.price;
    }
    public String getType() {
        return this.type;
    }
    public String getCreated_at() {
        return this.created_at;
    }
}
