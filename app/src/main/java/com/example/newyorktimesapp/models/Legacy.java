package com.example.newyorktimesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Legacy {
    @SerializedName("xlarge")
    @Expose
    private String xlarge;
    @SerializedName("xlargewidth")
    @Expose
    private int xlargewidth;
    @SerializedName("xlargeheight")
    @Expose
    private int xlargeheight;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("thumbnailwidth")
    @Expose
    private int thumbnailwidth;
    @SerializedName("thumbnailheight")
    @Expose
    private int thumbnailheight;
    @SerializedName("widewidth")
    @Expose
    private int widewidth;
    @SerializedName("wideheight")
    @Expose
    private int wideheight;
    @SerializedName("wide")
    @Expose
    private String wide;

    public String getXlarge() {
        return xlarge;
    }

    public void setXlarge(String xlarge) {
        this.xlarge = xlarge;
    }

    public int getXlargewidth() {
        return xlargewidth;
    }

    public void setXlargewidth(int xlargewidth) {
        this.xlargewidth = xlargewidth;
    }

    public int getXlargeheight() {
        return xlargeheight;
    }

    public void setXlargeheight(int xlargeheight) {
        this.xlargeheight = xlargeheight;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getThumbnailwidth() {
        return thumbnailwidth;
    }

    public void setThumbnailwidth(int thumbnailwidth) {
        this.thumbnailwidth = thumbnailwidth;
    }

    public int getThumbnailheight() {
        return thumbnailheight;
    }

    public void setThumbnailheight(int thumbnailheight) {
        this.thumbnailheight = thumbnailheight;
    }

    public int getWidewidth() {
        return widewidth;
    }

    public void setWidewidth(int widewidth) {
        this.widewidth = widewidth;
    }

    public int getWideheight() {
        return wideheight;
    }

    public void setWideheight(int wideheight) {
        this.wideheight = wideheight;
    }

    public String getWide() {
        return wide;
    }

    public void setWide(String wide) {
        this.wide = wide;
    }

}
