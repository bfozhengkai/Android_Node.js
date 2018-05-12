package com.kaisizhenghw9.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TextView name;
    private DetailPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mPager;


    public static double lat;
    public static double lng;
    public static String startLocName;
    private ImageView tweet;
    private ImageView favor;
    private ImageView returnToResult;
    public  static String responseJson;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail_activity);
        context = this;
        mAdapter = new DetailPagerAdapter(getSupportFragmentManager());
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.getTabAt(0).setCustomView(mAdapter.getTabView(0));
        mTabLayout.getTabAt(1).setCustomView(mAdapter.getTabView(1));
        mTabLayout.getTabAt(2).setCustomView(mAdapter.getTabView(2));
        mTabLayout.getTabAt(3).setCustomView(mAdapter.getTabView(3));
        ImageButton tweet = (ImageButton)findViewById(R.id.tweetButton);
        tweet.setImageResource(R.drawable.share);

        returnToResult = (ImageButton) findViewById(R.id.returnToResult);
        returnToResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        String jsonObject = getIntent().getStringExtra("detailjson");
       try {
        responseJson = jsonObject;
        JSONObject json = new JSONObject(jsonObject);
        final JSONObject result = json.getJSONObject("result");
        final String pid = result.getString("place_id");
        name = (TextView) findViewById(R.id.nameId);
        startLocName =result.getString("name");
         name.setText(result.getString("name"));
        lat = result.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        lng = result.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        database = getSharedPreferences("favorInfo", MODE_PRIVATE);
        final ImageButton favor = (ImageButton)findViewById(R.id.favorButton);
        if (database.contains(pid)){
            favor.setImageResource(R.drawable.heart_fill_white);
        }

        else {
            favor.setImageResource(R.drawable.heart_outline_white);
        }
        favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (database.contains(pid)){
                    favor.setImageResource(R.drawable.heart_outline_white);

                    Toast.makeText(DetailsActivity.this, startLocName+" was removed from favorites.",Toast.LENGTH_LONG).show();
                   String pid="";
                    try {
                        editor = database.edit();
                        pid = result.getString("place_id");
                        editor.remove(pid);
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listremovePid(pid);

                }

                else {
                    favor.setImageResource(R.drawable.heart_fill_white);
                    Toast.makeText(DetailsActivity.this, startLocName+" was added to favorites.",Toast.LENGTH_LONG).show();

                    String add = null;
                    try {
                        add = result.getString("formatted_address");
                        String pname = result.getString("name");
                        String picon = result.getString("icon");
                        String pid = result.getString("place_id");
                        database = getSharedPreferences("favorInfo",MODE_PRIVATE);
                        editor = database.edit();
                        String input = picon + "@#!!!" + pname + "@#!!!" + add + "@#!!!" + pid;
                        editor.putString(pid, input);
                        editor.commit();
                        PlacesItem pitem = new PlacesItem(picon, pname,add,pid);
                        Fragment2.list.add(pitem);
                        Fragment2.adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }
        });
        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/intent/tweet?text=";
                try {
                    String web = result.has("website") ? result.getString("website"):result.getString("url");
                    url+= "Check out "+result.getString("name")+" located at "+result.getString("formatted_address")+". Website: "+web;
                    url.replaceAll(" ", "+");
                    System.out.println("url");
                    Uri uri = Uri.parse(url);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

//
//            result.getString("formatted_address");
//            if (json.has("formatted_phone_number")){
//                result.getJSONArray("formatted_phone_number");
//            }
//            result.getJSONObject("geometry");
//
//
//            result.getJSONArray("photos");
//            result.getString("rating");
//            result.getString("price_level");
//            result.getString("url");//google websit;
//            result.getString("website");

       } catch (JSONException e) {
            e.printStackTrace();
       }
    }
    private void listremovePid(String pid){


        List<PlacesItem> list = Fragment2.list;
        for (int i=0;i<list.size();i++) {
            if(list.get(i).getPlace_id().equals(pid)){
                Fragment2.list.remove(i);
            }
        }
        Fragment2.adapter.notifyDataSetChanged();
    }

}
class DetailPagerAdapter extends FragmentPagerAdapter {
    String symbol = "";

    public DetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                    CurrentView tab1 = new CurrentView();
                return DetailFragment1.newInstance(0);
            case 1:
//                    HistoricalView tab2  = new HistoricalView();
                return DetailFragment2.newInstance(1);
            case 2:
//                    HistoricalView tab2  = new HistoricalView();
                return DetailFragment3.newInstance(2);
            case 3:
//                    HistoricalView tab2  = new HistoricalView();
                return DetailFragment4.newInstance(3);
        }
        return DetailFragment1.newInstance(0);
    }

    @Override
    public int getCount() {

        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Info";
            case 1:
                return "photo";
            case 2:
                return "Map";
            case 3:
                return "Reviews";
        }
        return null;
    }
    public View getTabView(int position) {
        View v = LayoutInflater.from(DetailsActivity.context).inflate(R.layout.bar_item, null);
        if (position==0) {

            ImageView im = (ImageView) v.findViewById(R.id.bar_Img);
            im.setImageResource(R.drawable.info_outline);
            TextView txt = (TextView) v.findViewById(R.id.bar_txt);
            txt.setText("Info");
        }
        else if (position==1) {
            ImageView im = (ImageView) v.findViewById(R.id.bar_Img);
            im.setImageResource(R.drawable.photos);
            TextView txt = (TextView) v.findViewById(R.id.bar_txt);
            txt.setText("Photo");
        }
        else if (position==2){
            ImageView im = (ImageView) v.findViewById(R.id.bar_Img);
            im.setImageResource(R.drawable.maps);
            TextView txt = (TextView) v.findViewById(R.id.bar_txt);
            txt.setText("Map");
        }
        else {
            ImageView im = (ImageView) v.findViewById(R.id.bar_Img);
            im.setImageResource(R.drawable.review);
            TextView txt = (TextView) v.findViewById(R.id.bar_txt);
            txt.setText("Reviews");
        }
        return v;
    }

}