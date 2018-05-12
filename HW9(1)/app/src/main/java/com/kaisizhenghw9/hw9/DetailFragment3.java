package com.kaisizhenghw9.hw9;


import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment3 extends Fragment implements OnMapReadyCallback,
GoogleApiClient.OnConnectionFailedListener{
    private static final LatLngBounds LAT_LNG_BOUNDS= new LatLngBounds(
            new LatLng(-40,-168), new LatLng(71,136));

    private static final java.lang.String ARG_PAGE = "arg_page";
    SupportMapFragment mapFragment;
    Spinner travelModeSpinner;
    ArrayAdapter<String> adapterList;
    List<String> list;
    private GoogleMap mMap;
    private String searchingMode;
    private AutoCompleteTextView mSearchText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private static String Mode;
    public static LatLng start;
    public static LatLng destination;
    private boolean first = true;
    public DetailFragment3() {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.detail_fragment3, container, false);
        travelModeSpinner = (Spinner) view.findViewById(R.id.travelModeSpinner);
        init();
        adapterList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        adapterList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.input_Search);
        travelModeSpinner.setAdapter(adapterList);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment=SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);



        travelModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (first) {
                    first=false;
                    return;
                } else {
                    if (position == 0) {

                        Mode = "driving";
                        System.out.println(Mode);
                        mMap.clear();
                        drawRoutes(start, destination, mMap, "driving");
                    } else if (position == 1) {
                        Mode = "bicycling";
                        System.out.println(Mode);
                        mMap.clear();
                        drawRoutes(start, destination, mMap, "bicycling");
                    } else if (position == 2) {
                        Mode = "transit";
                        System.out.println(Mode);
                        mMap.clear();
                        drawRoutes(start, destination, mMap, "transit");
                    } else {
                        Mode = "walking";
                        System.out.println(Mode);
                        mMap.clear();
                        drawRoutes(start, destination, mMap, "walking");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;

    }

    public static Fragment newInstance(int pageNumber){
        DetailFragment3 myFragment = new DetailFragment3();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE, pageNumber);
        myFragment.setArguments(arguments);
        return  myFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(DetailsActivity.lat,DetailsActivity.lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        MarkerOptions options = new MarkerOptions().position(latLng).title(DetailsActivity.startLocName);
        googleMap.addMarker(options);
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient,LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        ||actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute our method for searchin
                    geoLocate();

                }


                return false;
            }
        });
    }
    private void geoLocate() {
        String searchString  = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> listOfAddr = new ArrayList<>();
        try {
            listOfAddr = geocoder.getFromLocationName(searchString,1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listOfAddr.size()>0) {

            Address address = listOfAddr.get(0);
            double lat = address.getLatitude();
            double lon = address.getLongitude();
            System.out.println("Found a location: "+ lat );
            System.out.println("Found a location: "+ lon );


        }
    }
    private void init() {
        list = new ArrayList<>();
        list.add("Drving");
        list.add("Bicycling");
        list.add("Transit");
        list.add("Walking");
    }

/*
----------------------------------------------google ------------------------------------------
 */private void hideSoftKeyboard() {
     getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
}
   private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideSoftKeyboard();

        final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
        final String placeId = item.getPlaceId();

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient,placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
    }

   };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()){
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {
                mPlace.setName(place.getName().toString());
                System.out.println("Fetching startLatLng");
                mPlace.setAddress(place.getAddress().toString());
//                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setRating(place.getRating());
                mPlace.setName(place.getName().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                mMap.clear();
                start = new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude);

                destination = new LatLng(DetailsActivity.lat, DetailsActivity.lng);


                drawRoutes(start,destination,mMap, "driving");
                /*
                ---------------------------------------draw routes--------------------------------
                 */

            }catch (NullPointerException e) {
                System.out.println();
            }

            mPlace = new PlaceInfo();
           /*
            https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyB5HpnNpiDMGXftWGmx0Fg6bfPJDfJ_WPE
             */




            places.release();
        }
    };
    private void drawRoutes(LatLng start, LatLng destination, GoogleMap mMap, String mode) {
        MarkerOptions optionsFrom = new MarkerOptions().position(start).title(mPlace.getName());
        mMap.addMarker(optionsFrom);
        MarkerOptions optionsDestination = new MarkerOptions().position(destination).title(DetailsActivity.startLocName);
        mMap.addMarker(optionsDestination);

        double startlat = start.latitude;
        double startlon = start.longitude;
        double destinationlat = destination.latitude;
        double destinationlon = destination.longitude;
       // https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling&key=AIzaSyB5HpnNpiDMGXftWGmx0Fg6bfPJDfJ_WPE
        String routesURL = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" +startlat+","+startlon;
        routesURL+="&destination=" +destinationlat+","+destinationlon;
        routesURL+="&mode="+mode;
        routesURL+="&key=AIzaSyB5HpnNpiDMGXftWGmx0Fg6bfPJDfJ_WPE";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, routesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] diList=null;
                        diList = processDirection(response);
                        displayTheRoutes(diList);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Request error!", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);

    }
    public void displayTheRoutes(String[] diList) {
        PolylineOptions polylineOptions = new PolylineOptions();
        int len = diList.length;
        for (int i=0;i<len;i++) {

            polylineOptions.color(Color.BLUE);
            polylineOptions.width(10);
            polylineOptions.addAll(PolyUtil.decode(diList[i]));


        }
        mMap.addPolyline(polylineOptions);
    }
    public String[] processDirection(String json){
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getroutes(jsonArray);
    }

    public String[] getroutes(JSONArray stepsJson) {
        if (stepsJson == null) {
            return null;
        }
        int len  = stepsJson.length();
        String[] polyLines = new String[len];
        for (int i=0;i<len;i++) {
            try {
                polyLines[i] = getPath(stepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return polyLines;
    }
    public String getPath(JSONObject pathJson) {
        String polyLine = "";
        try {
            polyLine = pathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyLine;
    }
}
