package Model;

public class ListItem {

    //declarations
    private String name;
    private String description;
    private String rating;

    //constructor
    public ListItem(String name, String description, String rating) {
        this.name = name;
        this.description = description;
        this.rating = rating;
    }

    //getter and setter for name and description
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
