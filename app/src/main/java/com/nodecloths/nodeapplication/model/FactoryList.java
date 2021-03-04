package com.nodecloths.nodeapplication.model;

public class FactoryList {

    private String id;
    private String mobile_number;
    private String poster_name;
    private String factory_name;
    private String factory_type;
    private String specific;
    private String compliance;
    private String production;
    private String order;
    private String location;
    private String description;
    private String picture;
    private String postType;

    public FactoryList(String id, String mobile_number, String poster_name,
                       String factory_name, String factory_type, String specific,
                       String compliance, String production, String order,
                       String location, String description, String picture,String type) {
        this.id = id;
        this.mobile_number = mobile_number;
        this.poster_name = poster_name;
        this.factory_name = factory_name;
        this.factory_type = factory_type;
        this.specific = specific;
        this.compliance = compliance;
        this.production = production;
        this.order = order;
        this.location = location;
        this.description = description;
        this.picture = picture;
        this.postType = type;
    }

    public String getId() {
        return id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getPoster_name() {
        return poster_name;
    }

    public String getFactory_name() {
        return factory_name;
    }

    public String getFactory_type() {
        return factory_type;
    }

    public String getSpecific() {
        return specific;
    }

    public String getCompliance() {
        return compliance;
    }

    public String getProduction() {
        return production;
    }

    public String getOrder() {
        return order;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getPostType() {
        return postType;
    }
}
