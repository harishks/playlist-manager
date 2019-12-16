package models;

/**
 * Created by hshankar on 12/12/19.
 */
public class Users {
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "models.Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
