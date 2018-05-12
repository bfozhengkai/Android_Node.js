package com.kaisizhenghw9.hw9;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends Activity{

    private String nextPageToken;
    private  String nextPageToken2;
    private RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter1;
    private static RecyclerView.Adapter adapter2;
    private static RecyclerView.Adapter adapter3;
    private List<PlacesItem> placesItemList1;
    private List<PlacesItem> placesItemList2;
    private List<PlacesItem> placesItemList3;
    private int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        final String jsonObject = getIntent().getStringExtra("json");
        recyclerView = (RecyclerView) findViewById(R.id.placesView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        placesItemList1 = new ArrayList<>();
        placesItemList2 = new ArrayList<>();
        placesItemList3 = new ArrayList<>();
        final Button preBtn = (Button) findViewById(R.id.previousButton);
        final ImageButton backWardBtn = (ImageButton) findViewById(R.id.backWard);
        final TextView noResult = (TextView) findViewById(R.id.noResults);
        backWardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("return");
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        preBtn.setEnabled(false);
        try {
            JSONObject json = new JSONObject(jsonObject);
            JSONArray results = json.getJSONArray("results");
            if (results==null || results.length()==0){
                noResult.setVisibility(View.VISIBLE);
            }
            if (json.has("next_page_token")){
                nextPageToken = json.getString("next_page_token");
            }
      //      nextPageToken =  json.getString("next_page_token") !=null ? json.getString("next_page_token") : "";
            for (int i=0;i<results.length();i++){
               String icon =  results.getJSONObject(i).getString("icon");
               String name =  results.getJSONObject(i).getString("name");
               String address =  results.getJSONObject(i).getString("vicinity");
               String place_id =  results.getJSONObject(i).getString("place_id");
               PlacesItem item = new PlacesItem(icon,name,address,place_id);
               placesItemList1.add(item);
            }
            adapter1 = new RecycleViewAdapter(placesItemList1,this);
            recyclerView.setAdapter(adapter1);
        } catch (JSONException e) {
            noResult.setVisibility(View.VISIBLE);
        }
        final Button nextBtn = (Button) findViewById(R.id.nextButton);
        if (nextPageToken==null || nextPageToken.length()==0){
            nextBtn.setEnabled(false);
        }
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                preBtn.setEnabled(true);
                if(page==1) {
                    page++;
                    if (placesItemList2 !=null && placesItemList2.size()!=0) {
                        if (nextPageToken2==null || nextPageToken2.length()==0){
                            nextBtn.setEnabled(false);
                        }
                        recyclerView.setAdapter(adapter2);
                        return;
                    }
                    else {
                        final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setMessage("Fetching next page");
                        progressDialog.show();
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                       String url_next = "http://newnodejs-env.us-west-2.elasticbeanstalk.com/next_token?token=";
                       url_next+=nextPageToken;
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_next,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject json = new JSONObject(response);
                                            JSONArray results = json.getJSONArray("results");
                                            if (json.has("next_page_token")){
                                                nextPageToken2 = json.getString("next_page_token");
                                            }
                                            else {
                                                System.out.println("no such next");
                                                nextBtn.setEnabled(false);
                                            }
                                            //      nextPageToken =  json.getString("next_page_token") !=null ? json.getString("next_page_token") : "";
                                            for (int i=0;i<results.length();i++){
                                                String icon =  results.getJSONObject(i).getString("icon");
                                                String name =  results.getJSONObject(i).getString("name");
                                                String address =  results.getJSONObject(i).getString("vicinity");
                                                String place_id =  results.getJSONObject(i).getString("place_id");
                                                PlacesItem item = new PlacesItem(icon,name,address,place_id);
                                                placesItemList2.add(item);
                                            }
                                            adapter2 = new RecycleViewAdapter(placesItemList2,v.getContext());
                                            recyclerView.setAdapter(adapter2);
                                        } catch (JSONException e) {
                                            noResult.setVisibility(View.VISIBLE);
                                            e.printStackTrace();
                                        }

                                        progressDialog.dismiss();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                            }
                        });
                        queue.add(stringRequest);


                        return;
                    }
                }
                if (page==2){
                    page++;
                    nextBtn.setEnabled(false);
                    if (placesItemList3!=null && placesItemList3.size()!=0){
                        recyclerView.setAdapter(adapter3);

                        return;
                    }
                    else {
                        final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setMessage("Fetching next page");
                        progressDialog.show();
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        String url_next = "http://newnodejs-env.us-west-2.elasticbeanstalk.com/next_token?token=";
                        url_next+=nextPageToken2;
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_next,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject json = new JSONObject(response);
                                            JSONArray results = json.getJSONArray("results");
                                            for (int i=0;i<results.length();i++){
                                                String icon =  results.getJSONObject(i).getString("icon");
                                                String name =  results.getJSONObject(i).getString("name");
                                                String address =  results.getJSONObject(i).getString("vicinity");
                                                String place_id =  results.getJSONObject(i).getString("place_id");
                                                PlacesItem item = new PlacesItem(icon,name,address,place_id);
                                                placesItemList3.add(item);
                                            }
                                            adapter3 = new RecycleViewAdapter(placesItemList3,v.getContext());
                                            recyclerView.setAdapter(adapter3);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        progressDialog.dismiss();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                            }
                        });
                        queue.add(stringRequest);

                        return;

                    }
                }

            }
        });

        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(page);
                if(page==2) {
                   // adapter1 = new RecycleViewAdapter(placesItemList1,v.getContext());
                    nextBtn.setEnabled(true);
                    recyclerView.setAdapter(adapter1);
                    preBtn.setEnabled(false);
                }
                if (page==3) {
                   // adapter2 = new RecycleViewAdapter(placesItemList2,v.getContext());
                    nextBtn.setEnabled(true);
                    recyclerView.setAdapter(adapter2);
                }
                page--;
            }
        });
    }


}
