package com.kaisizhenghw9.hw9;

import android.app.Application;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{
    private List<PlacesItem> placesItems;
    private Context context;

    private SharedPreferences.Editor editor;
    private SharedPreferences database;

    public RecycleViewAdapter(List<PlacesItem> placesItems, Context context) {

        this.placesItems = placesItems;
        this.context = context;
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_layout,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewAdapter.ViewHolder holder, final int position) {
       final PlacesItem placesItem = placesItems.get(position);
       holder.textAdress.setText(placesItem.getAddress());
       holder.textName.setText(placesItem.getName());
        Picasso.get().load(placesItem.getIcon()).into(holder.imageIcon);
        database=context.getSharedPreferences("favorInfo", Context.MODE_PRIVATE);
        String val = database.getString(placesItem.getPlace_id(),null);
        if(val!=null){
            System.out.println("show red heart");
            holder.favorIcon.setImageResource(R.drawable.heart_fill_red);

        }
        else {
            holder.favorIcon.setImageResource(R.drawable.heart_outline_black);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processDetail(placesItem.getPlace_id());


            }
        });
        holder.favorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if not favor
                database = context.getSharedPreferences("favorInfo", Context.MODE_PRIVATE);
                if (!database.contains(placesItem.getPlace_id())) {
                    holder.favorIcon.setImageResource(R.drawable.heart_fill_red);
                    Toast.makeText(context, placesItem.getName()+" was added to favorites.",Toast.LENGTH_LONG).show();
                    Fragment2.list.add(placesItem);
                    Fragment2.adapter.notifyDataSetChanged();
                    System.out.println("favor");
                    String add = placesItem.getAddress();
                    String pname = placesItem.getName();
                    String picon = placesItem.getIcon();
                    String pid = placesItem.getPlace_id();
                    database = context.getSharedPreferences("favorInfo", context.MODE_PRIVATE);
                    editor = database.edit();
                    String input = picon + "@#!!!" + pname + "@#!!!" + add + "@#!!!" + pid;
                    editor.putString(pid, input);
                    editor.commit();
                }
                else {
                    holder.favorIcon.setImageResource(R.drawable.heart_outline_black);
                    Toast.makeText(context, placesItem.getName()+" was removed from favorites.",Toast.LENGTH_LONG).show();
                    System.out.println("Remove");
                    editor = database.edit();
                    editor.remove(placesItem.getPlace_id());
                    editor.commit();
                    Fragment2.list.remove(position);
                    Fragment2.adapter.notifyDataSetChanged();
                }//else
            }
        });

    }
    private void processDetail(String place_id) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Fetching results");
        progressDialog.show();
        String urlplaceId = "http://newnodejs-env.us-west-2.elasticbeanstalk.com/placeId?placeId=";
        urlplaceId+=place_id;
        System.out.println(urlplaceId);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlplaceId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // System.out.println(response);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("detailjson", response);
                        context.startActivity(intent);
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("You put the wrong placeID.");
            }
        });


        queue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return placesItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textAdress;
        public TextView textName;
        public ImageView imageIcon;
        public LinearLayout linearLayout;
        public ImageButton favorIcon;
        public ViewHolder(View itemView) {
            super(itemView);

            textAdress = (TextView) itemView.findViewById(R.id.name);
            textName = (TextView) itemView.findViewById(R.id.address);
            imageIcon = (ImageView) itemView.findViewById(R.id.categoryIcon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            favorIcon = (ImageButton) itemView.findViewById(R.id.favorIcon);
        }
    }
}
