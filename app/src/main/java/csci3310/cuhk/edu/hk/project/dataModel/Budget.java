package csci3310.cuhk.edu.hk.project.dataModel;

/**
 * Created by csamyphew on 16/12/15.
 */
public class Budget {
    int id;
    double amount;
    String type; //budget for specific category or whole budget(e.g Cat1, Cat2, All)

    //constructors
    public Budget() {
    }
    public Budget(double amount, String type) {
        this.amount = amount;
        this.type = type;
    }
    public Budget(int id, double amount, String type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setType(String type) {
        this.type = type;
    }

    //getters
    public long getId() {
        return this.id;
    }
    public double getAmount() {
        return this.amount;
    }
    public String getType() {
        return this.type;
    }
}
