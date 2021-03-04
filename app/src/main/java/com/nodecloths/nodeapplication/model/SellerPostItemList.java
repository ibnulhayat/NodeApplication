package com.nodecloths.nodeapplication.model;

public class SellerPostItemList {

    private String id;
    private String phoneNum;
    private String postStatus;
    private String fabricName;
    private String fabricType;
    private String fabric_G_min;
    private String fabric_G_max;
    private String weight_min;
    private String weight_max;
    private String fab_colorName;
    private String fab_colorCode;
    private String location;
    private String price;
    private String message;
    private String date;
    private String visivility;

    public SellerPostItemList(String id, String phoneNum, String postStatus, String fabricName,
                              String fabricType, String fabric_G_min, String fabric_G_max,
                              String weight_min, String weight_max, String fab_colorName,
                              String fab_colorCode, String location, String price,
                              String message, String date,String visivility) {
        this.id = id;
        this.phoneNum = phoneNum;
        this.postStatus = postStatus;
        this.fabricName = fabricName;
        this.fabricType = fabricType;
        this.fabric_G_min = fabric_G_min;
        this.fabric_G_max = fabric_G_max;
        this.weight_min = weight_min;
        this.weight_max = weight_max;
        this.fab_colorName = fab_colorName;
        this.fab_colorCode = fab_colorCode;
        this.location = location;
        this.price = price;
        this.message = message;
        this.date = date;
        this.visivility = visivility;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public String getFabricName() {
        return fabricName;
    }

    public String getFabricType() {
        return fabricType;
    }

    public String getFabric_G_min() {
        return fabric_G_min;
    }

    public String getFabric_G_max() {
        return fabric_G_max;
    }

    public String getWeight_min() {
        return weight_min;
    }

    public String getWeight_max() {
        return weight_max;
    }

    public String getFab_colorName() {
        return fab_colorName;
    }

    public String getFab_colorCode() {
        return fab_colorCode;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getVisivility() {
        return visivility;
    }
}
