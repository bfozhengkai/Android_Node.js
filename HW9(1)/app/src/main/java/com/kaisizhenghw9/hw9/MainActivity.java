package com.kaisizhenghw9.hw9;

import android.Manifest;
import android.app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Spinner spinner;
    ArrayAdapter<String> adapter;
    private List<String> list;
    ViewPager container;
    EditText mkeyword;
    EditText mdistance;
    AutoCompleteTextView mcurrLoc;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public int selectLoc = 0;
    public String currentLoc = "";
    public String keyword = "";
    public String distance = "10";
    public String from = "";
    public static double lat=34.0266;
    public static double lng=-118.283;
    public String[] cate = new String[]{"default", "airport", "amusement_park", "aquarium",
            "art_gallery", "bakery", "bar", "beauty_salon", "bowling_alley", "bus_station",
            "cafe", "campground", "car_rental", "casino", "lodging", "movie_theater",
            "museum", "night_club", "park", "parking", "restaurant", "shopping_mall", "stadium"
            , "subway_station", "taxi_stand", "train_station", "transit_station", "travel_agency",
            "zoo"};
    public String selectedCate = "";
    public int selectedPosition = 0;
    private FusedLocationProviderClient mFusedLocationClient;
 //   private SharedPreferences database;
    private TabLayout mTabLayout;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
//        database = getSharedPreferences("favorInfo", this.MODE_PRIVATE);
//        database.edit().clear().commit();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Oninit();
        //init();


        container = (ViewPager) findViewById(R.id.container);
        FragmentManager fm = getSupportFragmentManager();
        mSectionsPagerAdapter = new SectionsPagerAdapter(fm);
        container.setAdapter(mSectionsPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_tab);
        mTabLayout.setupWithViewPager(container);
        mTabLayout.getTabAt(0).setCustomView(mSectionsPagerAdapter.getTabView(0));
        mTabLayout.getTabAt(1).setCustomView(mSectionsPagerAdapter.getTabView(1));
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                container.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void Oninit() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You don't have the permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    9003);
        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED)

        mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lat = location.getLatitude();
                    System.out.println(lat);
                    lng = location.getLongitude();
                    System.out.println(lng);
                    // Logic to handle location object


                }
            }
        });


    }

    //    private void init() {
//        Button btnMap = (Button) findViewById(R.id.btnMap);
//        btnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(intent1);
//            }
//        });
//    }
    public void changeToResults(View view) {
        Fragment1 fragment = (Fragment1) getSupportFragmentManager().findFragmentById(R.id.fragment1);

        mkeyword = (EditText) findViewById(R.id.keyword);
        if (mkeyword.getText()==null || mkeyword.getText().toString().length()==0){

            mkeyword.setError("Please enter mandatory field",null);
            return;
        }

        keyword = mkeyword.getText().toString();
        System.out.println(mkeyword.getText().toString());

        Spinner category = (Spinner) findViewById(R.id.spinner);
        int position = category.getSelectedItemPosition();
        selectedPosition = position;
        System.out.println(cate[selectedPosition]);
        selectedCate = cate[selectedPosition];

        mdistance = (EditText) findViewById(R.id.dis);
        if (mdistance.getText()!=null && mdistance.getText().toString().length()!=0) {
            distance = String.valueOf(mdistance.getText().toString());
        }
        if (selectLoc == 0) {

            processJson1(lat, lng);

        } else {
            mcurrLoc = (AutoCompleteTextView) findViewById(R.id.hereLocation);
            if (mcurrLoc.getText()==null || mcurrLoc.getText().toString().length()==0){

                mcurrLoc.setError("Please enter mandatory field",null);
                return;
            }
            from = mcurrLoc.getText().toString();
            System.out.println(from);
            processJson2();
//            urlnear = 'http://newnodejs-env.us-west-2.elasticbeanstalk.com/nearby?lat=';
//            urlnear+=lat+"&lng="+lng;
//            urlnear+="&radius="+distance+"&type="+cate+"&keyword="+this.keywordElementRef.nativeElement.value;
        }
//        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
//        startActivity(intent);
    }

    private void processJson1(double lat, double lng) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Fetching results");
        progressDialog.show();
        // DownloadTask task = new DownloadTask();
        int juli = Integer.parseInt(distance) * 1609;
        String urlnear = "http://newnodejs-env.us-west-2.elasticbeanstalk.com/nearby?lat=";
        urlnear += lat + "&lng=" + lng;
        urlnear += "&radius=" + juli + "&type=" + selectedCate + "&keyword=" + keyword;
        System.out.println(urlnear);

        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlnear,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // System.out.println(response);
                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        intent.putExtra("json", response);

                        startActivity(intent);
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Request error!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void processJson2() {
        EditText otherplace = (EditText) findViewById(R.id.hereLocation);
        String otherp = otherplace.getText().toString();
        otherp= otherp.replaceAll(" ","+");
        String url = "http://newnodejs-env.us-west-2.elasticbeanstalk.com/request?request=";
        url+=otherp;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // System.out.println(response);
                        try {
                            JSONObject res  = new JSONObject(response);
                            System.out.println(response);
                           lat = res.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");

                           lng= res.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                           processJson1(lat,lng);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
//                        intent.putExtra("json", response);
//                        startActivity(intent);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Request error!", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);

    }

    public void currentLocOption1(View view) {
        selectLoc = 0;
        System.out.println(selectLoc);
        EditText currLoc = (AutoCompleteTextView) findViewById(R.id.hereLocation);
        currLoc.setEnabled(false);
    }

    public void currentLocOption2(View view) {
        selectLoc = 1;
        System.out.println(selectLoc);
        EditText currLoc = (AutoCompleteTextView) findViewById(R.id.hereLocation);
        currLoc.setEnabled(true);
    }

    public void clear(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
//public void clear(View view) {
//
//    EditText kword = (EditText) findViewById(R.id.keyword);
//    kword.setText("");
//    kword.setError(null);
//}
    public void changeToSearch(View view) {
        container.setCurrentItem(0);
    }


    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: google play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);

        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {
    String symbol = "";

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                    CurrentView tab1 = new CurrentView();
                return Fragment1.newInstance();
            case 1:
//                    HistoricalView tab2  = new HistoricalView();
                return Fragment2.newInstance();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Search";
            case 1:
                return "Favorites";

        }
        return null;
    }
    public View getTabView(int position) {
        View v = LayoutInflater.from(MainActivity.context).inflate(R.layout.main_item, null);
        if (position==0) {

            TextView txt = (TextView) v.findViewById(R.id.main_txt);
            txt.setText("SEARCH");
        }
        else {
            ImageView im = (ImageView) v.findViewById(R.id.main_Img);
            im.setImageResource(R.drawable.heart_fill_white);
            TextView txt = (TextView) v.findViewById(R.id.main_txt);
            txt.setText("FAVORITES");
        }
        return v;
    }

}
//         <option value="default">Default</option>
//          <option value="airport">Airport</option>
//          <option value="amusement_park">Amusement Park</option>
//          <option value="aquarium">Aquarium</option>
//          <option value="art_gallery">Art Gallery</option>
//          <option value="bakery">Bakery</option>
//          <option value="bar">Bar</option>
//          <option value="beauty_salon">Beauty Salon</option>
//          <option value="bowling_alley">Bowling Alley</option>
//          <option value="bus_station">Bus Station</option>
//          <option value="cafe">Cafe</option>
//          <option value="campground">Campground</option>
//          <option value="car_rental">Car Rental</option>
//          <option value="casino">Casino</option>
//          <option value="lodging">Lodging</option>
//          <option value="movie_theater">Movie Theater</option>
//          <option value="museum">Museum</option>
//          <option value="night_club">Night Club</option>
//          <option value="park">Park</option>
//          <option value="parking">Parking</option>
//          <option value="restaurant">Restaurant</option>
//          <option value="shopping_mall">Shopping Mall</option>
//          <option value="stadium">Stadium</option>
//          <option value="subway_station">Subway Station</option>
//          <option value="taxi_stand">Taxi Stand</option>
//          <option value="train_station">Train Station</option>
//          <option value="transit_station">Transit Station</option>
//          <option value="travel_agency">Travel Agency</option>
//          <option value="zoo">Zoo</option>