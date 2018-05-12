package com.kaisizhenghw9.hw9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<PhotoItem> list;
    private Context context;

    public PhotoAdapter(List<PhotoItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotoItem photoItem = list.get(position);

        //
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=750" +
                "&photoreference=";
        url+=photoItem.getImageURL();
        url+= "&key=AIzaSyB5HpnNpiDMGXftWGmx0Fg6bfPJDfJ_WPE";
        Picasso.get().load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.photoItem);
        }
    }
}
