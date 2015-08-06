package com.chunyuedu.traveltrail.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.chunyuedu.traveltrail.R;
import com.chunyuedu.traveltrail.entities.ParseMarkerObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MarkerPagerActivity extends FragmentActivity {
    ViewPager mViewPager;
    public static ParseMarkerObject theMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        final List<ParseMarkerObject> markers = getMarkers();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return markers.size();
            }
            @Override
            public Fragment getItem(int pos) {
                String markerId = markers.get(pos).getObjectId();
                return MarkerFragment.newInstance(markerId);
            }
        });
        String markerId = getIntent().getStringExtra(ParseMarkerObject.Extra.IMAGE_POSITION);

        for (int i = 0; i < markers.size(); i++) {
            if (markers.get(i).getObjectId().equals(markerId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }



    private List<ParseMarkerObject> getMarkers() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseMarkerObject> query = ParseQuery.getQuery(ParseMarkerObject.class);
        query.whereEqualTo("username", currentUser.getUsername());
        List<ParseMarkerObject> results = new ArrayList<ParseMarkerObject>();

        try {
            results=query.find();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

}
