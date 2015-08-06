package com.chunyuedu.traveltrail.adapter;

import android.location.Address;

import com.chunyuedu.traveltrail.DatabaseConnection.ParseDBInterface;
import com.chunyuedu.traveltrail.DatabaseConnection.ParseDatabaseHelper;
import com.chunyuedu.traveltrail.entities.Marker;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;

/**
 * Created by ChunyueDu on 7/20/15.
 */
public abstract class ProxyMarker {
    private static LinkedHashMap<String, Marker> markers;
    public ProxyMarker() {
        markers = new LinkedHashMap<String, Marker>();
    }


    public void BuildAMarker(LatLng position, String filename, boolean showup, Address oneAddress, byte[] data, String type){
        Marker marker = new Marker(position, filename, showup, oneAddress);
        if(markers == null) {
            markers = new LinkedHashMap<String, Marker>();
        }

        markers.put(marker.getFileName(), marker);
        ParseDBInterface parseDB = new ParseDatabaseHelper();
        parseDB.save(marker, data);

    }
    public void DeleteMarker(String marker){
        if(markers.containsKey(marker)) {
            markers.remove(marker);

        }

    }

}
