package com.kaisizhenghw9.hw9;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    List<ReviewsItem> listItems;
    private Context context;


    public ReviewsAdapter(List<ReviewsItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reviews_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewsItem item = listItems.get(position);
        holder.reviewName.setText(item.getReviewName());
        holder.reviewTxt.setText(item.getReviewTxt());
        holder.ratingBar.setRating(item.getReviewRating());
        SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Long time = new Long(item.getReviewTime())*1000;
        String formatTime = format.format(time);
        holder.reviewTime.setText(formatTime);
        if(item.getReviewIcon() !=null){
            Picasso.get().load(item.getReviewIcon()).resize(200,200).into(holder.imageView);
        }
        final String url = item.getAuthor_url();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView reviewName;
        public TextView reviewTxt;
        public TextView reviewTime;
        public ImageView imageView;
        public RatingBar ratingBar;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            reviewName = (TextView) itemView.findViewById(R.id.reviewName);
            reviewTxt = (TextView) itemView.findViewById(R.id.reviewTxt);
            reviewTime = (TextView) itemView.findViewById(R.id.reviewTime);
            imageView = (ImageView) itemView.findViewById(R.id.reviewIcon);
            ratingBar = (RatingBar) itemView.findViewById(R.id.reviewRating);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.review_item);
        }
    }
}
