package com.chunyuedu.traveltrail.adapter;
/**
 * Created by ChunyueDu on 7/20/15.
 */
import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;


public interface CreateMarkerInterface {
    public void BuildAMarker(LatLng position, String filename, boolean showup, Address oneAddress, byte[] data);
}
