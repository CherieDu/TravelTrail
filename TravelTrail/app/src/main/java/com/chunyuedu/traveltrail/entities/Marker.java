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
    public final String pictureResourceURL;
    public final String customizedTitle;
//    private String thrumbnail;
    private boolean showup;

    public final int profilePhoto;

    public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";


    public Marker(LatLng position, String fileName, int pictureLocalResource) {
        this.fileName = fileName;
        this.mPosition = position;
        profilePhoto = pictureLocalResource;
        pictureResourceURL = "";
        this.customizedTitle = "";
    }


    public Marker(LatLng position, String customizedTitle, int pictureLocalResource, String filename, String pictureResource, boolean showup) {
        this.customizedTitle = customizedTitle;
        this.fileName = filename;
        this.pictureResourceURL = pictureResource;
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
