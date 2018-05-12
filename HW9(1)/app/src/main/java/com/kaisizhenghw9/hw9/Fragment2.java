package com.kaisizhenghw9.hw9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fragment2 extends Fragment {

    private SharedPreferences database;
    private SharedPreferences.Editor editor;
    public static List<PlacesItem> list;
    private RecyclerView favorRecycler;
    public static RecyclerView.Adapter adapter;
    private TextView nofavor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment2, container, false);
        list = new ArrayList<>();
        createRecyclerView(v);

        return v;
    }



    public void createRecyclerView(View v) {
        favorRecycler = (RecyclerView) v.findViewById(R.id.favorRecyclerView);
        favorRecycler.setHasFixedSize(true);
        favorRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        database = getActivity().getSharedPreferences("favorInfo", getContext().MODE_PRIVATE);
        Map<String, ?> map = database.getAll();
        if (map == null || map.size()==0) {
            TextView nofavor = v.findViewById(R.id.noFavorites);
            nofavor.setText("No favorites");
            nofavor.setVisibility(View.VISIBLE);

        }


            for (Map.Entry<String, ?> entry: map.entrySet()){
                if (entry !=null) {
                    String val = entry.getValue().toString();

                    String[] strs = val.split("@#!!!");
                    //  System.out.println(strs[2]);
                    // System.out.println(strs[3]);
                    list.add(new PlacesItem(strs[0],strs[1],strs[2],strs[3]));
                }
            }
            adapter = new RecycleViewAdapter(list, getContext());
            favorRecycler.setAdapter(adapter);
    }
    public static Fragment newInstance() {
        return new Fragment2();
    }
}
