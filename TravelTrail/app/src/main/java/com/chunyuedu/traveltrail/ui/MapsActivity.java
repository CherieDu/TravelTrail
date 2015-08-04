package com.chunyuedu.traveltrail.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chunyuedu.traveltrail.R;
import com.chunyuedu.traveltrail.entities.Marker;
import com.chunyuedu.traveltrail.entities.Person;
import com.chunyuedu.traveltrail.entities.UrlHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

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
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LocationListener locationListener = null;
    LocationManager locationManager;
    String provider;
    private static final String IMAGE_DIRECTORY_NAME = "TravelTrail";

    private ClusterManager<Marker> mClusterManager;
    private Random mRandom = new Random(1984);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        setUpMapIfNeeded();

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
                startDemo();
            }
        }
    }

    /**
     * Run the demo-specific code.
     */

    protected void startDemo() {


        List<ParseObject> results = getMarkers();
        Log.i("Size of Markers", Integer.toString(results.size()));


//        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.4435262, -79.9444015), 9.5f));

        mClusterManager = new ClusterManager<Marker>(this, getMap());
        mClusterManager.setRenderer(new MarkerRenderer());
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
//        mClusterManager.setOnClusterClickListener(this);
//        mClusterManager.setOnClusterInfoWindowClickListener(this);
//        mClusterManager.setOnClusterItemClickListener(this);
//        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

//        addItems();

        for (ParseObject parseObject : results){
            double theLat = parseObject.getDouble("currentLatitude");
            double theLon = parseObject.getDouble("currentLongitude");
            LatLng theLatLng = new LatLng(theLat, theLon);
            String tmpurl = parseObject.getParseFile("mediaurl").getUrl();
            mClusterManager.addItem(new Marker(theLatLng, "Walter", R.drawable.walter, tmpurl, true));
        }


        mClusterManager.cluster();
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


        LatLng latLng = null;
        latLng = new LatLng(currentLatitude, currentLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        MarkerOptions options = new MarkerOptions().position(latLng);
        mMap.addMarker(options);
//        mMap.setMyLocationEnabled(true);
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//
//        // Getting the name of the best provider
//        provider = locationManager.getBestProvider(criteria, true);
//
//        // Getting Current Location and add a marker
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        double currentLatitude = location.getLatitude();
//        double currentLongitude = location.getLongitude();
//        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//        MarkerOptions options = new MarkerOptions().position(latLng);
//        mMap.addMarker(options);
//
//        mMap.setMyLocationEnabled(true);
//
//
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    protected int getLayoutId() {
        return R.layout.activity_maps;
    }

    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return mMap;
    }

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class MarkerRenderer extends DefaultClusterRenderer<Marker> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public MarkerRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Marker marker, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            //        String tmpurl = "http://files.parsetfss.com/e6d83aff-fc05-4a4e-895a-53e7bcd85620/tfss-66f69276-66d1-4dac-988c-95ae441c5691-IMG_20150804_102557.jpg";
//        videoPreview.setVisibility(View.GONE);
//
//        imgPreview.setVisibility(View.VISIBLE);
            ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
            imageLoader.displayImage(marker.pictureResourceURL, mImageView);
            Log.i("onBeforeClusterItemRend", marker.pictureResourceURL);

//            mImageView.setImageResource(marker.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(marker.fileName);
        }

//        @Override
//        protected void onBeforeClusterRendered(Cluster<Marker> cluster, MarkerOptions markerOptions) {
//            // Draw multiple people.
//            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
//            List<Drawable> pictureResourceURLs = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
//            int width = mDimension;
//            int height = mDimension;
//
//            for (Marker m : cluster.getItems()) {
//                // Draw 4 at most.
//                if (pictureResourceURLs.size() == 4) break;
//
//                UrlHelper urlHelper = new UrlHelper();
////                Drawable drawable = urlHelper.getImageDrawable(m.pictureResourceURL);
//
////                Drawable drawable = getDrawableFromUrl(m.pictureResourceURL);
////                Drawable drawable = mImageView.getDrawable();
//                Drawable drawable = getResources().getDrawable(m.profilePhoto);
//                drawable.setBounds(0, 0, width, height);
//                pictureResourceURLs.add(drawable);
//
//            }
//            MultiDrawable multiDrawable = new MultiDrawable(pictureResourceURLs);
//            multiDrawable.setBounds(0, 0, width, height);
//
//            mClusterImageView.setImageDrawable(multiDrawable);
//            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//        }

        public Drawable drawableFromUrl(String url) throws IOException {
            Bitmap x;

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();

            x = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(x);
        }


        public Drawable getDrawableFromUrl(URL url) {
            try {
                InputStream is = url.openStream();
                Drawable d = Drawable.createFromStream(is, "src");
                return d;
            } catch (MalformedURLException e) {
                // e.printStackTrace();
            } catch (IOException e) {
                // e.printStackTrace();
            }
            return null;
        }


        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }




    }

    private void addItems() {
        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        mClusterManager.addItem(new Marker(position(), "Walter", R.drawable.walter));

        // http://www.flickr.com/photos/usnationalarchives/4726917149/
        mClusterManager.addItem(new Marker(position(), "Gran", R.drawable.gran));

        // http://www.flickr.com/photos/nypl/3111525394/
        mClusterManager.addItem(new Marker(position(), "Ruth", R.drawable.ruth));

        // http://www.flickr.com/photos/smithsonian/2887433330/
        mClusterManager.addItem(new Marker(position(), "Stefan", R.drawable.stefan));

        // http://www.flickr.com/photos/library_of_congress/2179915182/
        mClusterManager.addItem(new Marker(position(), "Mechanic", R.drawable.mechanic));

        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
        mClusterManager.addItem(new Marker(position(), "Yeats", R.drawable.yeats));

        // http://www.flickr.com/photos/sdasmarchives/5036231225/
        mClusterManager.addItem(new Marker(position(), "John", R.drawable.john));

        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
        mClusterManager.addItem(new Marker(position(), "Trevor the Turtle", R.drawable.turtle));

        // http://www.flickr.com/photos/usnationalarchives/4726892651/
        mClusterManager.addItem(new Marker(position(), "Teach", R.drawable.teacher));
    }

    private LatLng position() {
        return new LatLng(random(40.4435262, -79.9444015), random(0.148271, -0.3514683));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }






}


