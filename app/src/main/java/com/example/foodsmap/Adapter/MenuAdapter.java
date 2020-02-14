package com.example.foodsmap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodsmap.MenuActivity;
import com.example.foodsmap.R;
import com.example.foodsmap.model.Menu;
import com.example.foodsmap.model.Post;
import com.example.foodsmap.model.Restaurant;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    public Context mContext;
    public List<Menu> mMenu;
    private FirebaseUser currentFirebaseUser;
    private  OnMenuListener mOnMenuListener;

    public MenuAdapter(Context mContext, List<Menu> mMenu, OnMenuListener onMenuListener) {
        this.mContext = mContext;
        this.mMenu = mMenu;
        this.mOnMenuListener=onMenuListener;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_card, viewGroup,false);
        return  new  MenuAdapter.ViewHolder(view, mOnMenuListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuAdapter.ViewHolder viewHolder, int i) {

        final Menu menu = mMenu.get(i);
        viewHolder.productName.setText(menu.getUrun());
        viewHolder.productDescription.setText(menu.getFiyat());
        Glide.with(mContext).load(menu.getImage_url()).apply(new RequestOptions().circleCrop()).into(viewHolder.productImage);



    }

    @Override
    public int getItemCount() {
        return mMenu.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView productName, productDescription;
        ImageView productImage, deleteproduct;
        com.example.foodsmap.model.Menu deneme;
        OnMenuListener onMenuListener;

        public ViewHolder(View itemView,OnMenuListener onMenuListener) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productDescription = (TextView) itemView.findViewById(R.id.productDescription);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            //deleteproduct = (ImageView) itemView.findViewById(R.id.deleteproduct);
           // deleteproduct.setOnClickListener(this);

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
