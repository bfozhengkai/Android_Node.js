package com.kaisizhenghw9.hw9;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DetailFragment4 extends Fragment {
    private static final java.lang.String ARG_PAGE = "arg_page";

    private JSONArray reviews;
    Spinner reviewSpinner;
    Spinner orderSpinner;
    ArrayAdapter<String> adapterList;
    ArrayAdapter<String> adapterOrder;
    List<String> list;
    List<String> orderList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<ReviewsItem> reviewsList;
    public DetailFragment4() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment4, container, false);
        final TextView noReviews = (TextView) view.findViewById(R.id.noReviews);

        //Review Spinner:
        reviewSpinner = (Spinner) view.findViewById(R.id.reviewSpinner);
        recyclerView = (RecyclerView) view.findViewById(R.id.reviewsView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsList = new ArrayList<>();

        init();
        adapterList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        adapterList.setDropDownViewResource(android.R.layout.simple_spinner_item);
        reviewSpinner.setAdapter(adapterList);
        reviewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You selected category：" + list.get(position), Toast.LENGTH_SHORT).show();
                JSONObject json = null;
                if(position == 0) {
                    //Process the google reviews
                    reviewsList.clear();
                    try {
                        json = new JSONObject(DetailsActivity.responseJson);
                        JSONObject result = json.getJSONObject("result");

                        if (result.has("reviews")) {
                            JSONArray reviews = result.getJSONArray("reviews");
                            for (int i = 0; i < 5; i++) {


                                System.out.println(i);
                                String txt = reviews.getJSONObject(i).getString("text");
                                String name = reviews.getJSONObject(i).getString("author_name");
                                String icon = reviews.getJSONObject(i).getString("profile_photo_url");
                                int rating = reviews.getJSONObject(i).getInt("rating");
                                String time = reviews.getJSONObject(i).getString("time");
                                String author_url = reviews.getJSONObject(i).getString("author_url");
                                ReviewsItem item = new ReviewsItem(icon,name,rating,time,txt,author_url,i);
                                reviewsList.add(item);
                                System.out.println(name);
                            }
                            adapter = new ReviewsAdapter(reviewsList, getContext());
                            recyclerView.setAdapter(adapter);
                        }
                        else {
                            noReviews.setVisibility(View.VISIBLE);
                            System.out.println("Show nothing.");
                        }

                    } catch (JSONException e) {
                        noReviews.setVisibility(View.VISIBLE);
                        System.out.println("Show nothing.");
                        e.printStackTrace();
                    }

                }
                else {
                    //Process the yelp reviews;
                    reviewsList.clear();
                    try {
                        json = new JSONObject(DetailsActivity.responseJson);
                        JSONObject result = json.getJSONObject("result");

                        String urlyelp = "http://newnodejs-env.us-west-2.elasticbeanstalk.com/yelp?name=";
                        String addr = result.getString("formatted_address");
                        String country = "US";
                        JSONArray address_components = result.getJSONArray("address_components");
                        int len = address_components.length();
                        String state = address_components.getJSONObject(len-3).getString("short_name");
                        String city = address_components.getJSONObject(len-4).getString("short_name");
                        urlyelp+=result.getString("name")+"&address1="+addr;
                        urlyelp+="&city="+city+"&state="+state+"&country="+country;
                        System.out.println(urlyelp);
                        processYelpBestMatch(urlyelp);


                        if (result.has("reviews")) {
//                            for (int i = 0; i < 5; i++) {
//
//
//                                System.out.println(i);
//                                String txt = reviews.getJSONObject(i).getString("text");
//                                String name = reviews.getJSONObject(i).getString("author_name");
//                                String icon = reviews.getJSONObject(i).getString("profile_photo_url");
//                                int rating = reviews.getJSONObject(i).getInt("rating");
//                                String time = reviews.getJSONObject(i).getString("time");
//                                String author_url = reviews.getJSONObject(i).getString("author_url");
//                                ReviewsItem item = new ReviewsItem(icon,name,rating,time,txt,author_url,i);
//                                reviewsList.add(item);
//                                System.out.println(name);
 //                           }
                            adapter = new ReviewsAdapter(reviewsList, getContext());
                            recyclerView.setAdapter(adapter);
                        }
//                        else {
////                            System.out.println("Show nothing.");
//                        }

                    } catch (JSONException e) {
                        noReviews.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Order Spinner:
        orderSpinner = (Spinner) view.findViewById(R.id.orderSpinner);
        adapterOrder = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, orderList);
        adapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_item);
        orderSpinner.setAdapter(adapterOrder);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You selected category：" + orderList.get(position), Toast.LENGTH_SHORT).show();



                    if(position==0) {

                        if (reviewsList != null && reviewsList.size() > 0) {
                            System.out.println("change" + position);
                            Collections.sort(reviewsList, new Comparator<ReviewsItem>() {
                                @Override
                                public int compare(ReviewsItem o1, ReviewsItem o2) {
                                    return o1.getOrder() - o2.getOrder();
                                }
                            });

                        }
                    }
                    else if(position==1) {

                        if (reviewsList != null && reviewsList.size() > 0) {
                            System.out.println("change" + position);
                            Collections.sort(reviewsList, new Comparator<ReviewsItem>() {
                                @Override
                                public int compare(ReviewsItem o1, ReviewsItem o2) {
                                    return o2.getReviewRating() - o1.getReviewRating();
                                }
                            });
                        }
                    }
//                        newList = reviewsList;
//                        reviewsList.clear();
//                        reviewsList.addAll(newList);
//                        adapter = new ReviewsAdapter(reviewsList, getContext());
//                        recyclerView.setAdapter(adapter);
                    else if(position==2){
                        if (reviewsList != null && reviewsList.size() > 0) {
                            System.out.println("change" + position);
                            Collections.sort(reviewsList, new Comparator<ReviewsItem>() {
                                @Override
                                public int compare(ReviewsItem o1, ReviewsItem o2) {
                                    return o1.getReviewRating() - o2.getReviewRating();
                                }
                            });
                        }
                    }
//                        newList = reviewsList;
//                        reviewsList.clear();
//                        reviewsList.addAll(newList);
//                        adapter = new ReviewsAdapter(reviewsList, getContext());
//                        recyclerView.setAdapter(adapter);
                    else if(position==3) {
                        if (reviewsList != null && reviewsList.size() > 0) {
                            System.out.println("change" + position);
                            Collections.sort(reviewsList, new Comparator<ReviewsItem>() {
                                @Override
                                public int compare(ReviewsItem o1, ReviewsItem o2) {
                                    Long res = new Long(o2.getReviewTime()) - new Long(o1.getReviewTime());
                                    if (res > 0) {
                                        return 1;
                                    } else if (res == 0) {
                                        return 0;
                                    } else {
                                        return -1;
                                    }
                                }
                            });

                        }
                    }
                    else {
                        if (reviewsList != null && reviewsList.size() > 0) {
                            System.out.println("change" + position);
                            Collections.sort(reviewsList, new Comparator<ReviewsItem>() {
                                @Override
                                public int compare(ReviewsItem o1, ReviewsItem o2) {
                                    Long res = new Long(o1.getReviewTime()) - new Long(o2.getReviewTime());
                                    if (res > 0) {
                                        return 1;
                                    } else if (res == 0) {
                                        return 0;
                                    } else {
                                        return -1;
                                    }
                                }
                            });

                        }
                    }
                adapter = new ReviewsAdapter(reviewsList, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });




        return view;

    }

    private void init() {
        list = new ArrayList<String>();
        list.add("Google reviews");
        list.add("Yelp reviews");
        orderList = new ArrayList<String>();
        orderList.add("Default order");
        orderList.add("Highest rating");
        orderList.add("Lowest rating");
        orderList.add("Most recent");
        orderList.add("Least recent");

    }

    public static Fragment newInstance(int pageNumber) {
        DetailFragment4 myFragment = new DetailFragment4();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE, pageNumber);
        myFragment.setArguments(arguments);
        return myFragment;
    }
    private void processYelpBestMatch(String urlyelp) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, urlyelp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject yelpjson = new JSONObject(response);
                            double latfromgoogle = DetailsActivity.lat;
                            double lonfromgoogle = DetailsActivity.lng;
                            double latitude = yelpjson.getJSONArray("businesses").getJSONObject(0).getJSONObject("coordinates").getDouble("latitude");
                            double longitude = yelpjson.getJSONArray("businesses").getJSONObject(0).getJSONObject("coordinates").getDouble("longitude");
                            System.out.println(latitude);
                            System.out.println(longitude);
                            if (latitude<=latfromgoogle+0.05 && latitude>=latfromgoogle-0.05 && longitude>=lonfromgoogle-0.05 && longitude<=lonfromgoogle+0.05) {
                                String id = yelpjson.getJSONArray("businesses").getJSONObject(0).getString("id");
                                getYelpReview(id);
                            }

                        } catch (JSONException e) {

                            System.out.println("Show nothing.");
                            return;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Request error!", Toast.LENGTH_SHORT).show();
                return;

            }
        });

        queue.add(stringRequest1);
    }
    private void getYelpReview(String id) {
        System.out.println(id);
        String yelpreviews="http://newnodejs-env.us-west-2.elasticbeanstalk.com/yelpReview?Id=";
        yelpreviews+=id;
        System.out.println(yelpreviews);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, yelpreviews,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject yelpjson = new JSONObject(response);
                           JSONArray reviewsArray = yelpjson.getJSONArray("reviews");
                           for (int i=0;i<reviewsArray.length();i++) {
                               JSONObject object = reviewsArray.getJSONObject(i);
                               if (object!=null){
                                   String icon = object.getJSONObject("user").getString("image_url");
                                   String name = object.getJSONObject("user").getString("name");
                                   int rating = object.getInt("rating");
                                   String time = object.getString("time_created");
                                   SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

                                   Date date = null;
                                   try {
                                       date = format.parse(time);
                                   } catch (ParseException e) {
                                       e.printStackTrace();
                                   }
                                   Long created_time = date.getTime();
                                   time = String.valueOf(created_time);
                                   String txt = object.getString("text");
                                   String author_url = object.getString("url");
                                   ReviewsItem item = new ReviewsItem(icon,name,rating,time,txt,author_url,i);
                                   reviewsList.add(item);
                               }

                           }
                            adapter = new ReviewsAdapter(reviewsList, getContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            System.out.println("Show nothing.");
                            return;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Request error!", Toast.LENGTH_SHORT).show();
                return;

            }
        });

        queue.add(stringRequest1);
    }

}
