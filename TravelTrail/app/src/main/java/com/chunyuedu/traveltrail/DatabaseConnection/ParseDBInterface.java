package com.chunyuedu.traveltrail.DatabaseConnection;

import com.chunyuedu.traveltrail.entities.Marker;

/**
 * Created by ChunyueDu on 8/1/15.
 */
public interface ParseDBInterface {
    public void save(Marker marker, byte[] data);
//    public void update(String modelName, Automobile automobile);
    public void delete(String fileName);
}
