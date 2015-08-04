package com.chunyuedu.traveltrail.entities;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ChunyueDu on 8/4/15.
 */

public class UrlHelper {
    private static final String TAG = UrlHelper.class.getSimpleName ( );

    public static URL createUrl ( String base, String action ) {
        return createUrl ( base, action, "" );
    }

    public static URL createUrl ( String base, String action, String additional ) {
        try {
            return new URL ( base + action + additional );
        } catch ( MalformedURLException ex ) {
            Log.e(TAG, ex.getMessage(), ex);
            return null;
        }
    }

    public static Uri createUri ( String base, String action ) {
        return createUri ( base, action, "" );
    }

    public static Uri createUri ( String base, String action, String additional ) {
        return Uri.parse ( base + action + additional );
    }

    public static URLConnection createConnection ( String baseUrl, String action ) throws IOException {
        return createConnection ( baseUrl, action, "" );
    }

    public static URLConnection createConnection ( String baseUrl, String action, String additional ) throws IOException {
        return createConnection ( createUrl ( baseUrl, action, additional ).toString ( ) );
    }

    public static URLConnection createConnection ( String url ) {
        Log.d ( TAG, "Creating Connection" );
        if ( url == null || url.length ( ) == 0 ) {
            Log.e ( TAG, "Unable to create url" );
            return null;
        } else {
            Log.d ( TAG, "Created url: " + url );
            try {
                URL turl = new URL ( url );
                return turl.openConnection ( );
            } catch ( MalformedURLException e ) {
                Log.e(TAG,e.getMessage ( ),e);
            } catch ( IOException e ) {
                Log.e(TAG,e.getMessage ( ),e);
            }
            return null;
        }
    }

//    /**
//     * Gets the image drawable from the url
//     *
//     * @author dustin.jorge@gmail.com
//     * @returns the Drawable image
//     * @param String
//     *          url The url of the image resource
//     */
    public static Drawable getImageDrawable ( String url ) {
        try {
            Log.d ( TAG, "Getting Drawable for StringUrl: " + url.toString ( ) );
            return getImageDrawable ( new URL ( url ) );
        } catch ( MalformedURLException e ) {
            Log.e(TAG,e.getMessage ( ),e);
        } catch ( Exception e ) {
            Log.e(TAG,e.getMessage ( ),e);
        }

        return null;
    }

//    /**
//     * Gets the image drawable from the url
//     *
//     * @author dustin.jorge@gmail.com
//     * @returns the Drawable image
//     * @param Url
//     *          url The url of the image resource
//     */
    public static Drawable getImageDrawable ( URL url ) {
        return getImageDrawable ( url,null,null );
    }

    public static Drawable getImageDrawable ( URL url, String username, String password ) {
        Drawable d = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient ( );
            HttpGet get = new HttpGet ( url.toURI ( ) );
            if ( username != null && username.length ( ) > 0 && password != null && password.length() > 0 ) {
                client.getCredentialsProvider ().setCredentials ( new AuthScope( url.getHost (), url.getPort () ), new UsernamePasswordCredentials( username, password ) );
            }
            HttpResponse response = client.execute ( get );
            InputStream is = response.getEntity ( ).getContent ( );
            BufferedInputStream bis = new BufferedInputStream ( is );
            String full = url.getFile ( );
            int start = full.lastIndexOf ( "/" ) + 1;
            if ( full.length ( ) > 0 ) {
                full = full.substring ( start );
            }
            d = Drawable.createFromStream ( bis, full );
            if ( d == null ) {
                Log.w ( TAG, "Drawable was null: " + full + ": " + url.toString ( ) );
            }
            bis.close ( );
            is.close ( );
        } catch ( Exception e ) {
            Log.e(TAG,e.getMessage ( ),e);
        }
        return d;
    }

}
