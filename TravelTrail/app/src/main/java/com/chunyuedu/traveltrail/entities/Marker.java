package com.chunyuedu.traveltrail.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

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
    public final String pictureResource;
//    private String thrumbnail;
    private boolean showup;

    public final int profilePhoto;
    public Marker(LatLng position, String fileName, int pictureLocalResource) {
        this.fileName = fileName;
        this.mPosition = position;
        profilePhoto = pictureLocalResource;
        pictureResource = "";
    }


    public Marker(LatLng position, String fileName, String pictureResource, boolean showup, int pictureLocalResource) {
        this.fileName = fileName;
        this.pictureResource = pictureResource;
        this.mPosition = position;
        this.showup = showup;
        profilePhoto = pictureLocalResource;

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
