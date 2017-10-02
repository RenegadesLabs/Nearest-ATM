package com.renegades.labs.nearestatm.api;

/**
 * Created by Виталик on 01.10.2017.
 */

public class Tw {
    private String hol;
    private String sat;
    private String thu;
    private String wed;
    private String mon;
    private String tue;
    private String sun;
    private String fri;

    public String getHol() {
        return hol;
    }

    public void setHol(String hol) {
        this.hol = hol;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getWed() {
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    @Override
    public String toString() {
        return "ClassPojo [hol = " + hol + ", sat = " + sat + ", thu = " + thu + ", wed = " + wed
                + ", mon = " + mon + ", tue = " + tue + ", sun = " + sun + ", fri = " + fri + "]";
    }
}
