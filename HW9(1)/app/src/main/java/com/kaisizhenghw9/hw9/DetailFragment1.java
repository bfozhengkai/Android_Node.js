package com.kaisizhenghw9.hw9;


import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetailFragment1 extends Fragment {


    private static final java.lang.String ARG_PAGE = "arg_page";

    public DetailFragment1() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment1, container, false);
        JSONObject json = null;
        JSONObject result=null;
        try {
            json = new JSONObject(DetailsActivity.responseJson);
            result = json.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
           String formatted_address = result.getString("formatted_address");
           TextView add = (TextView)view.findViewById(R.id.infoAddress);
           add.setText(formatted_address);

        } catch (JSONException e) {
            System.out.println("Show nothing.");
            e.printStackTrace();
        }
        try {
            String phone_number = result.getString("formatted_phone_number");
            TextView phone = (TextView)view.findViewById(R.id.infoPhone);
            phone.setText(phone_number);

        } catch (JSONException e) {
            LinearLayout main = (LinearLayout) view.findViewById(R.id.InfoLayOut);
            main.removeView(view.findViewById(R.id.phone));
            e.printStackTrace();
        }
        try {
            int price= result.getInt("price_level");
            TextView priceLevel = (TextView)view.findViewById(R.id.infoPrice);
            String money ="";
            for (int i=0;i<price;i++) {
                money+="$";
            }
            priceLevel.setText(money);

        } catch (JSONException e) {
            LinearLayout main = (LinearLayout) view.findViewById(R.id.InfoLayOut);
            main.removeView(view.findViewById(R.id.priceBar));
            System.out.println("Show nothing.");
            e.printStackTrace();
        }
        try {
            double price= result.getDouble("rating");
            RatingBar starRating =  (RatingBar) view.findViewById(R.id.starRating);
            float pri = new Float(price);
            starRating.setRating(pri);

        } catch (JSONException e) {
            System.out.println("Show nothing.");
            LinearLayout main = (LinearLayout) view.findViewById(R.id.InfoLayOut);
            main.removeView(view.findViewById(R.id.textView9));
            main.removeView(view.findViewById(R.id.starRating));
            e.printStackTrace();
        }
        try {
            String googleweb= result.getString("url");
           TextView googleWeb =  (TextView) view.findViewById(R.id.infoGooglePage);

            googleWeb.setText(googleweb);

        } catch (JSONException e) {
            System.out.println("Show nothing.");
            e.printStackTrace();
        }
        try {
            String websit= result.getString("website");
            TextView web =  (TextView) view.findViewById(R.id.infoWeb);

            web.setText(websit);

        } catch (JSONException e) {
            System.out.println("Show nothing.");
            LinearLayout main = (LinearLayout) view.findViewById(R.id.InfoLayOut);
            main.removeView(view.findViewById(R.id.WEB));

        }
     return view;
    }

    public static Fragment newInstance(int pageNumber){
        DetailFragment1 myFragment = new DetailFragment1();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE, pageNumber);
        myFragment.setArguments(arguments);
        return  myFragment;
    }
}
