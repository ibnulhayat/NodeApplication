package com.nodecloths.nodeapplication.model;

public class CommentsList {

    private String id;
    private String post_id;
    private String sellerNumber;
    private String sellerName;
    private String fabricePrice;
    private String sellerLocation;

    public CommentsList(String id, String post_id, String sellerNumber, String sellerName, String fabricePrice, String sellerLocation) {
        this.id = id;
        this.post_id = post_id;
        this.sellerNumber = sellerNumber;
        this.sellerName = sellerName;
        this.fabricePrice = fabricePrice;
        this.sellerLocation = sellerLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getSellerNumber() {
        return sellerNumber;
    }

    public void setSellerNumber(String sellerNumber) {
        this.sellerNumber = sellerNumber;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getFabricePrice() {
        return fabricePrice;
    }

    public void setFabricePrice(String fabricePrice) {
        this.fabricePrice = fabricePrice;
    }

    public String getSellerLocation() {
        return sellerLocation;
    }

    public void setSellerLocation(String sellerLocation) {
        this.sellerLocation = sellerLocation;
    }
}
