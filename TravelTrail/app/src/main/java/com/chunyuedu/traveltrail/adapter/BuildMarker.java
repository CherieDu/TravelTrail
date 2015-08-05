package com.chunyuedu.traveltrail.adapter;

import android.content.Context;

/**
 * Created by ChunyueDu on 7/20/15.
 */
public class BuildMarker extends ProxyMarker implements DeleteMarkerInterface
        , CreateMarkerInterface{
    private static BuildMarker mBuildMarker;
    public static BuildMarker get() {
        if (mBuildMarker == null) {
            mBuildMarker = new BuildMarker();
        }
        return mBuildMarker;
    }
}
