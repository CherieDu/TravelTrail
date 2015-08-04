package com.chunyuedu.traveltrail.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chunyuedu.traveltrail.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationListener locationListener = null;
    LocationManager locationManager;
    String provider;
    private static final String IMAGE_DIRECTORY_NAME = "TravelTrail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        setUpMapIfNeeded();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setUpMapIfNeeded();
//    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
//    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location and add a marker
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        List<ParseObject> results = getMarkers();

        Log.i("Size of Markers", Integer.toString(results.size()));
        LatLng latLng = null;
        latLng = new LatLng(currentLatitude, currentLongitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        MarkerOptions options = new MarkerOptions().position(latLng);
        map.addMarker(options);
        for (int i = 0; i < results.size(); i++){

            Log.i("1 result mediatype", String.valueOf(results.get(i).getString("mediatype")));
            double theLat = results.get(i).getDouble("currentLatitude");
            double theLon = results.get(i).getDouble("currentLongitude");
            LatLng theLatLng = new LatLng(theLat, theLon);
            Log.i("currentLatitude", String.valueOf(results.get(i).getDouble("currentLatitude")));
//            Log.i("currentLatitude", String.valueOf(results.get(i).getDate("mediaurl")));
            String tmpurl = results.get(i).getParseFile("mediaurl").getUrl();
            Log.i("tmpurl", tmpurl);
            try {
                Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(theLat, theLon, 1);
                if (!addresses.isEmpty()) {
                    if (addresses.size() > 0) {
                        String customizeTitle = addresses.get(0).getFeatureName()
                                + ", " + addresses.get(0).getLocality()
                                + ", " + addresses.get(0).getAdminArea()
                                + ", " + addresses.get(0).getCountryCode()
                                + ", " + addresses.get(0).getPostalCode();


                        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
                        Bitmap bmp = imageLoader.loadImageSync("http://files.parsetfss.com/e6d83aff-fc05-4a4e-895a-53e7bcd85620/tfss-8345042c-87d3-404a-81e0-aac46b9fd791-IMG_20150801_150358.jpg");



                        MarkerOptions theoptions = new MarkerOptions()
                                .position(theLatLng)
                                .title(customizeTitle);
                        //.icon(BitmapDescriptorFactory.fromBitmap(bmp));
                        map.addMarker(theoptions);


                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Bitmap bmp = getBitmapFromURL(tmpurl);
//            bmp = Bitmap.createScaledBitmap(bmp, 50, 50, true);

// //BitmapFactory.decodeStream(tmpurl.openConnection().getInputStream());

//            latLng = new LatLng(results.get(i).getDouble("currentLatitude"), results.get(i).getDouble("currentLongitude"));
//            MarkerOptions tmpOption = new MarkerOptions().position(latLng);
//            map.addMarker(tmpOption);
//

//            try {
//                bmp = BitmapFactory.decodeStream((InputStream)new URL(tmpurl).getContent());
//                bmp = Bitmap.createScaledBitmap(bmp, 50, 50, true);
//                MarkerOptions tmpOption = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bmp));
//                map.addMarker(tmpOption);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }



//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//            Bitmap tmp = results.get(i).getParseFile("mediaurl");
//            ParseFile masterImage = results.get(i).getParseFile("mediaurl");
//            Uri imageUri = Uri.parse(masterImage.getUrl());




//            MarkerOptions optionsTmp = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromPath();


        }

//        latLng = new LatLng(currentLatitude, currentLongitude);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//        MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
//        .fromPath("/sdcard/Pictures/TravelTrail/IMG_20150729_183726.jpg"));
//        map.addMarker(options);


//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);



//        MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
//                .fromPath("/sdcard/Pictures/TravelTrail/IMG_20150729_183726.jpg"));
//                map.addMarker(options);



        //.icon(BitmapDescriptorFactory.fromFile("file:///storage/emulated/0/Pictures/TravelTrail/IMG_20150729_183726.jpg")


//    }


    private List<ParseObject> getMarkers() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Marker");
        query.whereEqualTo("username", currentUser.getUsername());
        List<ParseObject> results = new ArrayList<ParseObject>();

        try {
            results=query.find();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return results;
    }



    private void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.«
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location and add a marker
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        MarkerOptions options = new MarkerOptions().position(latLng);
        mMap.addMarker(options);

        mMap.setMyLocationEnabled(true);


        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    protected int getLayoutId() {
        return R.layout.activity_maps;
    }
//
//    protected GoogleMap getMap() {
//        setUpMapIfNeeded();
//        return mMap;
//    }



}
