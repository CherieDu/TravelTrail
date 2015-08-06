package com.chunyuedu.traveltrail.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chunyuedu.traveltrail.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class AccountFragment extends Fragment implements View.OnClickListener {

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    private TextView name;
    private TextView email;
    private Button logoutBtn;
    private ImageView mImageView;
    private Button imageBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ParseUser currentUser = ParseUser.getCurrentUser();

//        currentUser.saveInBackground();
        name = (TextView)v.findViewById(R.id.userName);
        email = (TextView)v.findViewById(R.id.userEmail);
        logoutBtn = (Button)v.findViewById(R.id.logoutBtn);
        mImageView = (ImageView)v.findViewById(R.id.avatar);
        loadProfileImageFromOnline();
        mImageView.setOnClickListener(this);
        name.setText(currentUser.getString("name"));
        email.setText(currentUser.getEmail());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseUser.logOut();
                getActivity().finish();
                // Perform action on click
            }
        });



        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                loadImagefromGallery(v);
                break;
        }
    }


    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                Bitmap mBitmap = BitmapFactory
                        .decodeFile(imgDecodableString);
                mImageView.setImageBitmap(mBitmap);

                saveProfileImage(mBitmap);



            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void loadProfileImageFromOnline(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.displayImage(currentUser.getParseFile("profileImage").getUrl(), mImageView);
    }

    private void saveProfileImage(Bitmap mBitmap){
        ParseUser currentUser = ParseUser.getCurrentUser();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data2 = stream.toByteArray();
        ParseFile file = new ParseFile(currentUser.getUsername() + ".jpg", data2);
        file.saveInBackground();

        currentUser.put("profileImage", file);

        currentUser.saveInBackground();
    }
}
