package com.kaisizhenghw9.hw9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final LatLngBounds LAT_LNG_BOUNDS= new LatLngBounds(
            new LatLng(-40,-168), new LatLng(71,136));
    private Spinner category;
    ArrayAdapter<String> adapter;
    private List<String> list;
    private Button searchButton;
    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    public static Fragment newInstance(){
        Fragment fragment = new Fragment1();
        return fragment;
    }
    public int selectPosition=0;

//    public void searchFunction(View view) {
//        Log.i("Info", "Button Pressed.");
//    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        initDatas();

        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.hereLocation);
        category = (Spinner) view.findViewById(R.id.spinner);
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"You selected category："+list.get(position),Toast.LENGTH_SHORT).show();
                selectPosition = position;
                System.out.println(selectPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectPosition = 0;

            }
        });
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient,LAT_LNG_BOUNDS,null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        return view;
    }



    public interface  CallBack {
        public void getResult(String result);
    }
    public void getData(CallBack callBack) {
        String msg = String.valueOf(selectPosition);
        callBack.getResult(msg);
    }

    private void initDatas() {
        list = new ArrayList<String>();
        list.add("Default");
        list.add("Airport");
        list.add("Amusement Park");
        list.add("Aquarium");
        list.add("Art Gallery");
        list.add("Bakery");
        list.add("Bar");
        list.add("Beauty Salon");
        list.add("Bowling Alley");
        list.add("Bus Station");
        list.add("Cafe");
        list.add("Campground");
        list.add("Car Rental");
        list.add("Casino");
        list.add("Lodging");
        list.add("Movie Theater");
        list.add("Museum");
        list.add("Night Club");
        list.add("Park");
        list.add("Parking");
        list.add("Restaurant");
        list.add("Shopping Mall");
        list.add("Stadium");
        list.add("Subway Station");
        list.add("Taxi Stand");
        list.add("Train Station");
        list.add("Transit Station");
        list.add("Travel Agency");
        list.add("Zoo");
    }

}
