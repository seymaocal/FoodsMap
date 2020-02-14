package com.example.foodsmap.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodsmap.R;
import com.example.foodsmap.model.Menu;
import com.example.foodsmap.model.Rating;
import com.example.foodsmap.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder>  {

    public Context mContext;
    public List<Restaurant> mRestaurantList;
    public ArrayList<Restaurant> mRestaurantList2;
    private ArrayList<Restaurant> filteredUserList;
    private FirebaseUser currentFirebaseUser;
    private RestaurantAdapter.OnMenuListener mOnMenuListener;


    public RestaurantAdapter(Context mContext, List<Restaurant> mRestaurantList, RestaurantAdapter.OnMenuListener onMenuListener) {
        this.mContext = mContext;
        this.mRestaurantList = mRestaurantList;
        this.mOnMenuListener=onMenuListener;

    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_card, viewGroup,false);
        return  new  RestaurantAdapter.ViewHolder(view, mOnMenuListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantAdapter.ViewHolder viewHolder, int i) {

        final Restaurant restaurant = mRestaurantList.get(i);
        viewHolder.productName.setText(restaurant.getName());
        viewHolder.productDescription.setText(restaurant.getAddress());
        Glide.with(mContext).load(restaurant.getImage_url()).apply(new RequestOptions().circleCrop()).into(viewHolder.productImage);


        DatabaseReference commentReadAddress = FirebaseDatabase.getInstance().getReference("Rating").child(restaurant.getName());
        commentReadAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float ratin_value=0; float Count = 0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Rating rating = snapshot.getValue(Rating.class);
                    Count++;
                    ratin_value+=rating.getRating();
                }
                float sonuc=(ratin_value/Count);

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

    public void filterList(ArrayList<Restaurant> filteredList){
        mRestaurantList=filteredList;
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView productName, productDescription;
        ImageView productImage, deleteproduct;
        private RatingBar ratingBar;
        com.example.foodsmap.model.Menu deneme;
        RestaurantAdapter.OnMenuListener onMenuListener;

        public ViewHolder(View itemView, RestaurantAdapter.OnMenuListener onMenuListener) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productDescription = (TextView) itemView.findViewById(R.id.productDescription);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            ratingBar=(RatingBar) itemView.findViewById(R.id.ratingBar);
            LayerDrawable stars=(LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            this.onMenuListener=onMenuListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            onMenuListener.onMenuClick(getAdapterPosition());
        }

    }
    public interface  OnMenuListener{
        void onMenuClick(int position);
    }

}