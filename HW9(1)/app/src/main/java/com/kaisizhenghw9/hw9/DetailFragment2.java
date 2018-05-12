package com.kaisizhenghw9.hw9;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment2 extends Fragment {
    private static final java.lang.String ARG_PAGE = "arg_page";

    public DetailFragment2() {

    }

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PhotoItem> listPhotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.photosView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listPhotos = new ArrayList<>();
        JSONObject json = null;
        try {
            json = new JSONObject(DetailsActivity.responseJson);
            JSONObject result = json.getJSONObject("result");
            if (result.has("photos")){
                JSONArray photos = result.getJSONArray("photos");
                for (int i=0;i<photos.length();i++) {

                    String photoRef =photos.getJSONObject(i).getString("photo_reference");
                    PhotoItem photo = new PhotoItem(photoRef);
                    listPhotos.add(photo);
                }
                adapter = new PhotoAdapter(listPhotos, getContext());
                recyclerView.setAdapter(adapter);
            }
            else {
                System.out.println("Show no photos.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    public static Fragment newInstance(int pageNumber){
        DetailFragment2 myFragment = new DetailFragment2();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE, pageNumber);
        myFragment.setArguments(arguments);
        return  myFragment;
    }
}
