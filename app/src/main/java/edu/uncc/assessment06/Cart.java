package edu.uncc.assessment06;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Cart implements Serializable {
    private String pid, name, img_url, price, description, review_count,added_by,item_id;


    public Cart(){

    }

    public Cart(JSONObject json) throws JSONException {

        this.pid = json.getString("pid");
        this.name = json.getString("name");
        this.img_url = json.getString("img_url");
        this.price = json.getString("price");
        this.description = json.getString("description");
        this.review_count = json.getString("review_count");
        this.added_by=json.getString("added_by");
        this.item_id= json.getString("item_id");
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }
}
