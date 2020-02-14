package com.example.foodsmap.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodsmap.R;
import com.example.foodsmap.model.Rating;
import com.example.foodsmap.model.Restaurant;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Restaurant_SaveAdapter  extends RecyclerView.Adapter<Restaurant_SaveAdapter.ViewHolder> {

    public Context mContext;
    public List<Restaurant> mRestaurantList;
    private FirebaseUser currentFirebaseUser;
    private Restaurant_SaveAdapter.OnRestaurantListener mOnRestaurantListener;

    public Restaurant_SaveAdapter(Context mContext, List<Restaurant> mRestaurantList, Restaurant_SaveAdapter.OnRestaurantListener onRestaurantListener) {
        this.mContext = mContext;
        this.mRestaurantList = mRestaurantList;
        this.mOnRestaurantListener = onRestaurantListener;

    }

    @NonNull
    @Override
    public Restaurant_SaveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_save_restaurant_card, viewGroup, false);
        return new Restaurant_SaveAdapter.ViewHolder(view, mOnRestaurantListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        final Restaurant restaurant = mRestaurantList.get(position);
        viewHolder.productName.setText(restaurant.getName());
        viewHolder.productDescription.setText(restaurant.getAddress());
        Glide.with(mContext).load(restaurant.getImage_url()).apply(new RequestOptions().circleCrop()).into(viewHolder.productImage);


        DatabaseReference commentReadAddress = FirebaseDatabase.getInstance().getReference("Rating").child(restaurant.getName());
        commentReadAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float ratin_value = 0;
                float Count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rating rating = snapshot.getValue(Rating.class);
                    Count++;
                    ratin_value += rating.getRating();
                }
                float sonuc = (ratin_value / Count);

                //ratingDegeri.setText(String.valueOf(sonuc));
                viewHolder.ratingBar.setRating(sonuc);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, productDescription;
        ImageView productImage, deleteproduct;
        private RatingBar ratingBar;
        com.example.foodsmap.model.Menu deneme;
        Restaurant_SaveAdapter.OnRestaurantListener onRestaurantListener;

        public ViewHolder(View itemView, Restaurant_SaveAdapter.OnRestaurantListener onRestaurantListener) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productDescription = (TextView) itemView.findViewById(R.id.productDescription);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            this.onRestaurantListener = onRestaurantListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            onRestaurantListener.onRestaurantClick(getAdapterPosition());
        }

    }
    public interface  OnRestaurantListener{
        void onRestaurantClick(int position);
    }
}
