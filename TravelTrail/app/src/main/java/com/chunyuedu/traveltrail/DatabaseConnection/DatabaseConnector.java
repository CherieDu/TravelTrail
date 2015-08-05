package com.chunyuedu.traveltrail.DatabaseConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ChunyueDu on 7/20/15.
 */
public class DatabaseConnector {
    // database name
    private static final String DATABASE_NAME = "StudentRecord";
    private SQLiteDatabase database; // database object
    private DatabaseOpenHelper databaseOpenHelper; // database helper

    // public constructor for DatabaseConnector
    public DatabaseConnector(Context context)
    {
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    } // end DatabaseConnector constructor

    // open the database connection
    public void open() throws SQLException
    {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    } // end method open

    // close the database connection
    public void close()
    {
        if (database != null)
            database.close(); // close the database connection
    } // end method close

    // inserts a new contact in the database
    public void insertMarker(String markerID, String filePath, String address,
                              boolean fileType, String city, String state, String country, boolean showup)
    {
        ContentValues newMarker = new ContentValues();
        newMarker.put("markerID", markerID);
        newMarker.put("filePath", filePath);
        newMarker.put("fileType", fileType);
        newMarker.put("address", address);
        newMarker.put("city", city);
        newMarker.put("state", state);
        newMarker.put("country", country);
        newMarker.put("showup", showup);

        open(); // open the database
        database.insert("markers", null, newMarker);
        close(); // close the database
    } // end method insertContact


    // return a Cursor with all contact information in the database
    public Cursor getAllMarker()
    {
        return database.query("markers", new String[] {"_id", "markerID", "filePath", "fileType", "address", "city", "state", "country", "showup"},
                null, null, null, null, "markerID");
    } // end method getAllContacts

    // return a Cursor with all contact information in the database
    public Cursor getShowableMarker()
    {
        return database.query("markers", new String[] {"_id", "markerID", "filePath", "fileType", "address", "city", "state", "country", "showup"},
                null, null, null, null, "markerID");
    } // end method getAllContacts


    // get a Cursor containing all information about the contact specified
    // by the given id
    public Cursor getOneMarker(long id)
    {
        return database.query(
                "markers", null, "_id=" + id, null,null, null, null, null);
    } // end method getOnContact



    // delete the contact specified by the given String name
    public void deleteMarker(long id)
    {
        open(); // open the database
        database.delete("markers", "_id=" + id, null);
        close(); // close the database
    } // end method deleteContact

    private class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        // public constructor
        public DatabaseOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        } // end DatabaseOpenHelper constructor

        // creates the contacts table when the database is created
        @Override
        public void onCreate(SQLiteDatabase db)
        {
        //_id", "markerID", "filePath", "fileType", "address", "city", "state", "country", "showup"},

                    // query to create a new table named contacts
            String createQuery = "CREATE TABLE markers" +
                    "(_id integer primary key autoincrement," +
                    "markerID TEXT, filePath REAL, fileType NUMERIC," +
                    "address REAL, city REAL, state REAL，country REAL, showup NUMERIC);";

            db.execSQL(createQuery); // execute the query
        } // end method onCreate

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        } // end method onUpgrade
    } // end class DatabaseOpenHelper



}
