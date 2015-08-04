package com.chunyuedu.traveltrail.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.net.URL;

/**
 * Created by ChunyueDu on 7/20/15.
 */
public class Marker implements ClusterItem {
//    private String address;
//    private String city;
//    private String state;
//    private String country;
//    private String zipcode;
    private final LatLng mPosition;
    public final String fileName;
    public final URL pictureResource;
//    private String thrumbnail;
    private boolean showup;


    public Marker(LatLng position, String fileName, URL pictureResource, boolean showup) {
        this.fileName = fileName;
        this.pictureResource = pictureResource;
        this.mPosition = position;
        this.showup = showup;
    }



    public boolean isShowup() {
        return showup;
    }

    public void setShowup(boolean showup) {
        this.showup = showup;
    }





    @Override
    public LatLng getPosition() {
        return mPosition;
    }

}
