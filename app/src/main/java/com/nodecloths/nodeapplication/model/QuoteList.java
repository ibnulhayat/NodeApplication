package com.nodecloths.nodeapplication.model;

import java.util.List;

public class QuoteList {

    private String id;
    private String mobile_number;
    private String item_name;
    private String fab_name;
    private String fab_type;
    private String gsm;
    private String design;
    private String quantity;
    private String size;
    private String measurement_type;
    private String description;
    private String img_1;
    private List<String> list;
    private String postType;

    public QuoteList(String id, String mobile_number, String item_name, String fab_name,
                     String fab_type, String gsm, String design, String quantity,
                     String size, String measurement_type, String description,
                     String img_1, List<String> list,String type) {
        this.id = id;
        this.mobile_number = mobile_number;
        this.item_name = item_name;
        this.fab_name = fab_name;
        this.fab_type = fab_type;
        this.gsm = gsm;
        this.design = design;
        this.quantity = quantity;
        this.size = size;
        this.measurement_type = measurement_type;
        this.description = description;
        this.img_1 = img_1;
        this.list = list;
        this.postType = type;
    }

    public String getId() {
        return id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getFab_name() {
        return fab_name;
    }

    public String getFab_type() {
        return fab_type;
    }

    public String getGsm() {
        return gsm;
    }

    public String getDesign() {
        return design;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getMeasurement_type() {
        return measurement_type;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_1() {
        return img_1;
    }

    public List<String> getList() {
        return list;
    }

    public String getPostType() {
        return postType;
    }
}
