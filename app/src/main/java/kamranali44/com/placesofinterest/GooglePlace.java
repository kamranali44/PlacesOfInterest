package kamranali44.com.placesofinterest;

/**
 * Created by alika on 24/02/2017.
 */

public class GooglePlace {
    private String id;
    private String place_id;
    private String srIcon;
    private String reference;
    private String vicinity;
    private String name;
    private String category;
    private String rating;
    private String open;

    public GooglePlace() {
        this.name = "";
        this.rating = "";
        this.open = "";
        this.setCategory("");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setOpenNow(String open) {
        this.open = open;
    }

    public String getOpenNow() {
        return open;
    }
}
