package com.chunyuedu.traveltrail.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.chunyuedu.traveltrail.R;
import com.chunyuedu.traveltrail.entities.ParseMarkerObject;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;


public class MarkerFragment extends Fragment {

    public static final String EXTRA_MARKER_ID = "markerintent.MARKER_ID";


    ParseMarkerObject mParseMarkerObject;
    CheckBox mShopUpCheckBox;


    public static MarkerFragment newInstance(String markerID) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MARKER_ID, markerID);
        MarkerFragment fragment = new MarkerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MarkerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_marker, parent, false);
        String markerId = (String)getArguments().getSerializable(EXTRA_MARKER_ID);
        mParseMarkerObject = getOneMarker(markerId);
//        Log.i("mParseMarkerObject oncreate", mParseMarkerObject.getObjectId());
        Log.i("mParseMarkerObject showup", String.valueOf(mParseMarkerObject.getBoolean("showup")));

        mShopUpCheckBox = (CheckBox)v.findViewById(R.id.checkBox);

        mShopUpCheckBox.setChecked(Boolean.valueOf(mParseMarkerObject.getBoolean("showup")));
        mShopUpCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                // set the crime's solved property
//                mParseMarkerObject.setSolved(isChecked);
                ParseQuery<ParseMarkerObject> query = ParseQuery.getQuery(ParseMarkerObject.class);
// Retrieve the object by id
                query.getInBackground(mParseMarkerObject.getObjectId(), new GetCallback<ParseMarkerObject>() {
                    @Override
                    public void done(ParseMarkerObject markerObject, com.parse.ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            markerObject.put("showup", isChecked);

                            markerObject.saveInBackground();
                        }
                    }
                });

            }
        });
        // Inflate the layout for this fragment
        return v;
    }


    private ParseMarkerObject getOneMarker(String markerId){

        ParseMarkerObject resultMarker = null;
        ParseQuery<ParseMarkerObject> query = ParseQuery.getQuery(ParseMarkerObject.class);
        try {
            resultMarker = query.get(markerId);
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        Log.i("getOneMarker", markerId);
        Log.i("getOneMarker2", resultMarker.getObjectId());
        return resultMarker;

    }




}
