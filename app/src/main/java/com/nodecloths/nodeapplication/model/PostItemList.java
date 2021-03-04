package com.nodecloths.nodeapplication.model;

public class PostItemList {

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
    private String message;
    private String date;
    private String comment;
    private String imgORcolor;
    private String postType;

    public PostItemList(String id, String phoneNum, String postStatus, String fabricName,
                        String fabricType, String fabric_G_min, String fabric_G_max, String
                                weight_min, String weight_max, String fab_colorName,
                        String fab_colorCode, String message,String date, String comment,
                        String imgORcolor,String postType) {
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
        this.message = message;
        this.date = date;
        this.comment = comment;
        this.imgORcolor = imgORcolor;
        this.postType = postType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }

    public String getFabricType() {
        return fabricType;
    }

    public void setFabricType(String fabricType) {
        this.fabricType = fabricType;
    }

    public String getFabric_G_min() {
        return fabric_G_min;
    }

    public void setFabric_G_min(String fabric_G_min) {
        this.fabric_G_min = fabric_G_min;
    }

    public String getFabric_G_max() {
        return fabric_G_max;
    }

    public void setFabric_G_max(String fabric_G_max) {
        this.fabric_G_max = fabric_G_max;
    }

    public String getWeight_min() {
        return weight_min;
    }

    public void setWeight_min(String weight_min) {
        this.weight_min = weight_min;
    }

    public String getWeight_max() {
        return weight_max;
    }

    public void setWeight_max(String weight_max) {
        this.weight_max = weight_max;
    }

    public String getFab_colorName() {
        return fab_colorName;
    }

    public void setFab_colorName(String fab_colorName) {
        this.fab_colorName = fab_colorName;
    }

    public String getFab_colorCode() {
        return fab_colorCode;
    }

    public void setFab_colorCode(String fab_colorCode) {
        this.fab_colorCode = fab_colorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImgORcolor() {
        return imgORcolor;
    }

    public void setImgORcolor(String imgORcolor) {
        this.imgORcolor = imgORcolor;
    }

    public String getPostType() {
        return postType;
    }
}
