package com.renegades.labs.nearestatm.api;

/**
 * Created by Виталик on 01.10.2017.
 */

public class Device {
    private String placeUa;
    private String fullAddressRu;
    private String cityRU;
    private String cityUA;
    private String fullAddressEn;
    private Tw tw;
    private String longitude;
    private String latitude;
    private String type;
    private String placeRu;
    private String fullAddressUa;
    private String cityEN;

    public String getPlaceUa() {
        return placeUa;
    }

    public void setPlaceUa(String placeUa) {
        this.placeUa = placeUa;
    }

    public String getFullAddressRu() {
        return fullAddressRu;
    }

    public void setFullAddressRu(String fullAddressRu) {
        this.fullAddressRu = fullAddressRu;
    }

    public String getCityRU() {
        return cityRU;
    }

    public void setCityRU(String cityRU) {
        this.cityRU = cityRU;
    }

    public String getCityUA() {
        return cityUA;
    }

    public void setCityUA(String cityUA) {
        this.cityUA = cityUA;
    }

    public String getFullAddressEn() {
        return fullAddressEn;
    }

    public void setFullAddressEn(String fullAddressEn) {
        this.fullAddressEn = fullAddressEn;
    }

    public Tw getTw() {
        return tw;
    }

    public void setTw(Tw tw) {
        this.tw = tw;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaceRu() {
        return placeRu;
    }

    public void setPlaceRu(String placeRu) {
        this.placeRu = placeRu;
    }

    public String getFullAddressUa() {
        return fullAddressUa;
    }

    public void setFullAddressUa(String fullAddressUa) {
        this.fullAddressUa = fullAddressUa;
    }

    public String getCityEN() {
        return cityEN;
    }

    public void setCityEN(String cityEN) {
        this.cityEN = cityEN;
    }

    @Override
    public String toString() {
        return "ClassPojo [placeUa = " + placeUa + ", fullAddressRu = " + fullAddressRu
                + ", cityRU = " + cityRU + ", cityUA = " + cityUA + ", fullAddressEn = "
                + fullAddressEn + ", tw = " + tw + ", longitude = " + longitude + ", latitude = "
                + latitude + ", type = " + type + ", placeRu = " + placeRu + ", fullAddressUa = "
                + fullAddressUa + ", cityEN = " + cityEN + "]";
    }
}
