package com.chunyuedu.traveltrail.adapter;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.chunyuedu.traveltrail.DBLayout.ParseDBInterface;
import com.chunyuedu.traveltrail.DBLayout.ParseDatabaseHelper;
import com.chunyuedu.traveltrail.entities.Marker;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ChunyueDu on 7/20/15.
 */
public abstract class ProxyMarker {



    private static LinkedHashMap<String, Marker> markers;
    public void BuildAMarker(LatLng position, String filename, boolean showup, Address oneAddress, byte[] data){
        Marker marker = new Marker(position, filename, showup, oneAddress);
        if(markers == null) {
            markers = new LinkedHashMap<String, Marker>();
        }

        markers.put(marker.getFileName(), marker);
        ParseDBInterface parseDB = new ParseDatabaseHelper();
        parseDB.save(marker, data);

    }
    public void DeleteMarker(String marker){


    }
}
