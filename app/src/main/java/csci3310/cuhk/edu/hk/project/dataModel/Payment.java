package csci3310.cuhk.edu.hk.project.dataModel;

/**
 * Created by csamyphew on 16/12/15.
 */
public class Payment {
    int id;
    String name;

    //constructors
    public Payment() {
    }
    public Payment(String name) {
        this.name = name;
    }
    public Payment(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    //getters
    public long getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
}
