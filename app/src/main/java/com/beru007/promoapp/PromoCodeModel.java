package com.beru007.promoapp;

public class PromoCodeModel {
    private String pid;
    private String title;
    private String end_skidka;
    private String skidka;
    private String rating;
    private String links;
    private String decsript;

    public PromoCodeModel(String pid, String title, String end_skidka, String skidka, String rating, String links, String decsript) {
        this.pid = pid;
        this.title = title;
        this.end_skidka = end_skidka;
        this.skidka = skidka;
        this.rating = rating;
        this.links = links;
        this.decsript = decsript;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnd_skidka() {
        return end_skidka;
    }

    public void setEnd_skidka(String end_skidka) {
        this.end_skidka = end_skidka;
    }

    public String getSkidka() {
        return skidka;
    }

    public void setSkidka(String skidka) {
        this.skidka = skidka;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getDecsript() {
        return decsript;
    }

    public void setDecsript(String decsript) {
        this.decsript = decsript;
    }

}
