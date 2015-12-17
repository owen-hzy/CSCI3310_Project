package csci3310.cuhk.edu.hk.project.dataModel;

/**
 * Created by csamyphew on 16/12/15.
 */
public class Category {
    int id;
    String name;

    //constructors
    public Category() {
    }
    public Category(String name) {
        this.name = name;
    }
    public Category(int id, String name) {
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
