package csci3310.cuhk.edu.hk.project.dataModel;

/**
 * Created by csamyphew on 16/12/15.
 */
public class Currency {
    int id;
    String name;
    double rate;

    //constructors
    public Currency() {

    }
    public Currency(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }
    public Currency(int id, String name, double rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName (String name) {
        this.name = name;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }

    //getters
    public long getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public double getRate() {
        return this.rate;
    }


}
