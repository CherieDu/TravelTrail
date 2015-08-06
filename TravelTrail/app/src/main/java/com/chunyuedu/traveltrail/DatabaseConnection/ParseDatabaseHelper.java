package com.chunyuedu.traveltrail.DatabaseConnection;

import com.chunyuedu.traveltrail.entities.Marker;
import com.chunyuedu.traveltrail.entities.ParseMarkerObject;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by ChunyueDu on 8/1/15.
 */
public class ParseDatabaseHelper implements ParseDBInterface{


    @Override
    public void save(Marker marker, byte[] data) {
        ParseFile file = new ParseFile(marker.getFileName(), data);
        file.saveInBackground();

        ParseMarkerObject parseMarker = new ParseMarkerObject();
        ParseUser currentUser = ParseUser.getCurrentUser();
        parseMarker.put("mediatype", "image");
        parseMarker.put("username",currentUser.getUsername());
        parseMarker.put("mediaurl", file);
        parseMarker.put("filename", file.getName());
//        marker.put("thumbnail", file2);
        parseMarker.put("showup", marker.getShowUp());
        parseMarker.put("address", marker.getAddress());
        parseMarker.put("city", marker.getCity());
        parseMarker.put("country", marker.getCountry());
        parseMarker.put("customizedTitle", marker.getCustomizedTitle());
        parseMarker.put("postalCode", marker.getPostalCode());
        parseMarker.put("latitude", marker.getLatitude());
        parseMarker.put("longitude", marker.getLongitude());
        parseMarker.put("data", data);
//        parseMarker.put("latlng", marker.getmPosition());
        parseMarker.put("state", marker.getState());
//        parseMarker.put("currentLatitude", currentLatitude);
//        parseMarker.put("currentLongitude", currentLongitude);

        parseMarker.saveInBackground();
    }

    @Override
    public void delete(String fileName) {

    }




}
