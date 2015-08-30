package com.chunyuedu.traveltrail.DatabaseConnection;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

/**
 * Created by ChunyueDu on 8/2/15.
 */
public class UserDBHelper {
    private static ParseUser mParseUser;

    public UserDBHelper() {
        mParseUser = ParseUser.getCurrentUser();
    }

    public void InitUser() {
        mParseUser = ParseUser.getCurrentUser();
    }

    public String getName(){
        return mParseUser.getString("name");
    }

    public String getUserName(){
        return mParseUser.getUsername();
    }

    public String getEmail(){
        return mParseUser.getEmail();
    }

    public void Logout(){
        mParseUser.logOut();
    }

    public void loadProfileImage(ImageView mImageView){
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        if (mParseUser.getParseFile("profileImage") != null){
            imageLoader.displayImage(mParseUser.getParseFile("profileImage").getUrl(), mImageView);
        }

    }

    public void saveProfileImage(Bitmap mBitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data2 = stream.toByteArray();
        ParseFile file = new ParseFile(mParseUser.getUsername() + ".jpg", data2);
        file.saveInBackground();

        mParseUser.put("profileImage", file);

        mParseUser.saveInBackground();
    }


//    public static byte[]getProfileImage(){
//        return mParseUser.getString("profileImage");
//    }

}
