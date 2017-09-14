package project.vehiclessharing.object;

/**
 * Created by Tuan on 12/07/2017.
 */

public class User {
    private String full_name;
    private String url_image;

    public User() {
    }

    public User(String full_name, String url_image) {
        this.full_name = full_name;
        this.url_image = url_image;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
