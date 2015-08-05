package com.chunyuedu.traveltrail.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.parse.ParseObject;

/**
 * Created by ChunyueDu on 8/5/15.
 */
public class ParseMarkerObject extends ParseObject implements ClusterItem{
    @Override
    public LatLng getPosition() {
        return new LatLng(this.getDouble("latitude"), this.getDouble("longitude"));
    }
}
