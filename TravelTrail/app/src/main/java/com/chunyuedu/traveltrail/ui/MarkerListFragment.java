package com.chunyuedu.traveltrail.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chunyuedu.traveltrail.R;
import com.chunyuedu.traveltrail.entities.ParseMarkerObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class MarkerListFragment extends Fragment{
    protected AbsListView listView;
    public static final int INDEX = 0;
    public static List<ParseMarkerObject> markers = null;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_gallery, container, false);
        markers = getMarkers();
        Log.i("markers.size():", String.valueOf(markers.size()));
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_gallery_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        listView = (ListView) rootView.findViewById(R.id.list);
        ((ListView) listView).setAdapter(new ImageAdapter(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startMarkerActivity(markers.get(position));
            }
        });
        return rootView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AnimateFirstDisplayListener.displayedImages.clear();
    }

    protected void startMarkerActivity(ParseMarkerObject marker) {
        Intent intent = new Intent(getActivity(), MarkerPagerActivity.class);
        intent.putExtra(ParseMarkerObject.Extra.IMAGE_POSITION, marker.getObjectId());
        startActivity(intent);
    }

    private void applyScrollListener() {
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
    }

    private static class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            markers = getMarkers();
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(20)).build();
        }

        @Override
        public int getCount() {
            return markers.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_image, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) view.findViewById(R.id.time);
                holder.location = (TextView) view.findViewById(R.id.location);
                holder.image = (ImageView) view.findViewById(R.id.image);
                holder.checkBox = (CheckBox) view.findViewById(R.id.showUpCheckBox);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            Date createDate = markers.get(position).getCreatedAt();
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = DATE_FORMAT.format(createDate);
            holder.time.setText(date);
            holder.location.setText(markers.get(position).getString("address") + " " +markers.get(position).getString("filename"));
            holder.checkBox.setChecked(markers.get(position).getBoolean("showup"));
//            Log.i("mediaurl", markers.get(position).getParseFile("mediaurl").getUrl());

            ImageLoader.getInstance().displayImage(markers.get(position).getParseFile("mediaurl").getUrl(), holder.image, options, animateFirstListener);

            return view;
        }
    }

    static class ViewHolder {
        TextView time;
        TextView location;
        ImageView image;
        CheckBox checkBox;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private static List<ParseMarkerObject> getMarkers() {
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

    // fake a network operation's delayed response
    // this is just for demonstration, not real code!
    private void refreshContent(){
        markers = getMarkers();
        ((ListView) listView).setAdapter(new ImageAdapter(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startMarkerActivity(markers.get(position));
            }
        });
        mSwipeRefreshLayout.setRefreshing(false);

    }



}
