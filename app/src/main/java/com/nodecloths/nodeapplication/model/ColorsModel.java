package com.nodecloths.nodeapplication.model;

public class ColorsModel {
    private String colorName;
    private String hexColorCode;

    public ColorsModel(String colorName, String hexColorCode) {
        this.colorName = colorName;
        this.hexColorCode = hexColorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getHexColorCode() {
        return hexColorCode;
    }

    public void setHexColorCode(String hexColorCode) {
        this.hexColorCode = hexColorCode;
    }
}
