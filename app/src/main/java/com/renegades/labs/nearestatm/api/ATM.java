package com.renegades.labs.nearestatm.api;

/**
 * Created by Виталик on 01.10.2017.
 */

public class ATM {
    private String address;
    private Device[] devices;
    private String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "ClassPojo [address = " + address + ", devices = " + devices + ", city = "
                + city + "]";
    }
}
