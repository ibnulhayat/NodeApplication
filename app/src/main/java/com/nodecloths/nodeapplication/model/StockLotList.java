package com.nodecloths.nodeapplication.model;

import java.util.List;

public class StockLotList {
    private String id;
    private String mobile_number;
    private String title;
    private String brand;
    private String size;
    private String fabrics;
    private String quantity;
    private String price;
    private String location;
    private String description;
    private String timeDate;
    private String big_img;
    private List<String> list;
    private String postType;

    public StockLotList(String id, String mobile_number, String title, String brand,
                        String size, String fabrics, String quantity, String price,
                        String location, String description, String timeDate, String big_img,
                        List<String> list,String type) {
        this.id = id;
        this.mobile_number = mobile_number;
        this.title = title;
        this.brand = brand;
        this.size = size;
        this.fabrics = fabrics;
        this.quantity = quantity;
        this.price = price;
        this.location = location;
        this.description = description;
        this.timeDate = timeDate;
        this.big_img = big_img;
        this.list = list;
        this.postType = type;
    }

    public String getId() {
        return id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getTitle() {
        return title;
    }

    public String getBrand() {
        return brand;
    }

    public String getSize() {
        return size;
    }

    public String getFabrics() {
        return fabrics;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public String getBig_img() {
        return big_img;
    }

    public List<String> getList() {
        return list;
    }

    public String getPostType() {
        return postType;
    }
}
