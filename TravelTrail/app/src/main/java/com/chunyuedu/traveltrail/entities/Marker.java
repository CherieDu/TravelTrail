package com.chunyuedu.traveltrail.entities;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

/**
 * Created by ChunyueDu on 7/20/15.
 */

public class Marker implements ClusterItem {

    private final String address;
    private final String city;
    private final String state;
    private final String country;
    private final String postalCode;
    private final LatLng mPosition;
    private final double latitude;
    private final double longitude;
    private final String fileName;
    private String pictureResourceURL;
    private final String customizedTitle;

    private boolean showUp;




    public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";

    public Marker(LatLng position, String filename, boolean showup, Address oneAddress) {
        this.fileName = filename;
        this.pictureResourceURL = "";
        this.mPosition = position;
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.showUp = showup;

        if (oneAddress != null) {
                this.address = oneAddress.getFeatureName();
                this.city = oneAddress.getLocality();
                this.state = oneAddress.getAdminArea();
                this.country = oneAddress.getCountryCode();
                this.postalCode = oneAddress.getPostalCode();

                this.customizedTitle = oneAddress.getFeatureName()
                        + ", " + oneAddress.getLocality()
                        + ", " + oneAddress.getAdminArea()
                        + ", " + oneAddress.getCountryCode()
                        + ", " + oneAddress.getPostalCode();

        }else{
            this.address = "";
            this.city = "";
            this.state = "";
            this.country = "";
            this.postalCode = "";
            this.customizedTitle = "";

        }

    }






    @Override
    public LatLng getPosition() {
        return mPosition;
    }


    public String getAddress() {
        return address;
    }



    public String getCity() {
        return city;
    }



    public String getState() {
        return state;
    }



    public String getCountry() {
        return country;
    }



    public String getPostalCode() {
        return postalCode;
    }


    public LatLng getmPosition() {
        return mPosition;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPictureResourceURL() {
        return pictureResourceURL;
    }

    public String getCustomizedTitle() {
        return customizedTitle;
    }



    public static String getImagePosition() {
        return IMAGE_POSITION;
    }
    public boolean getShowUp() {
        return showUp;
    }

    public void setShowup(boolean showup) {
        this.showUp = showup;
    }
}
