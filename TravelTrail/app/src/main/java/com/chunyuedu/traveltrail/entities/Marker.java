package com.chunyuedu.traveltrail.entities;

/**
 * Created by ChunyueDu on 7/20/15.
 */
public class Marker {
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;

    public String getThrumbnail() {
        return thrumbnail;
    }

    public void setThrumbnail(String thrumbnail) {
        this.thrumbnail = thrumbnail;
    }

    public boolean isShowup() {
        return showup;
    }

    public void setShowup(boolean showup) {
        this.showup = showup;
    }

    private String thrumbnail;
    private boolean showup;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


}
