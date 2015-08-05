package com.chunyuedu.traveltrail.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ChunyueDu on 8/5/15.
 */
@ParseClassName("MyMarker")
public class ParseMarkerObject extends ParseObject implements ClusterItem{
    public ParseMarkerObject() {
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(this.getDouble("latitude"), this.getDouble("longitude"));
    }
}
