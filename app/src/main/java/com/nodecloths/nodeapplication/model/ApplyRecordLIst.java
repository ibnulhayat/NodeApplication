package com.nodecloths.nodeapplication.model;

public class ApplyRecordLIst {

    private String id;
    private String applyPost_id;
    private String number;

    public ApplyRecordLIst(String id, String applyPost_id, String number) {
        this.id = id;
        this.applyPost_id = applyPost_id;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public String getApplyPost_id() {
        return applyPost_id;
    }

    public String getNumber() {
        return number;
    }
}
