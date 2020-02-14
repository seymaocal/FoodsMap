package com.example.foodsmap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsmap.R;
import com.example.foodsmap.model.Menu;
import com.example.foodsmap.model.MyCommission;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MyCommisionAdapter extends RecyclerView.Adapter<MyCommisionAdapter.ViewHolder> {

    public Context mContext;
    public List<MyCommission> myCommissions;
    private FirebaseUser currentFirebaseUser;


    public MyCommisionAdapter(Context mContext, List<MyCommission> myCommissions) {
        this.mContext = mContext;
        this.myCommissions = myCommissions;
    }

    @NonNull
    @Override
    public MyCommisionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mycommision_card, viewGroup,false);
        return  new  MyCommisionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCommisionAdapter.ViewHolder viewHolder, int i) {

        final MyCommission myCommission = myCommissions.get(i);
        viewHolder.sender.setText(myCommission.getSender());
        viewHolder.resturantName.setText(myCommission.getRestaurant_name());
        viewHolder.foodName.setText(myCommission.getFood());
        viewHolder.pay.setText(myCommission.getPay());

    }

    @Override
    public int getItemCount() {
        return myCommissions.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView sender, resturantName,foodName,pay;
        ImageView productImage, deleteproduct;
        com.example.foodsmap.model.Menu deneme;


        public ViewHolder(View itemView) {
            super(itemView);
            sender = (TextView) itemView.findViewById(R.id.sender);
            resturantName = (TextView) itemView.findViewById(R.id.resturantName);
            foodName = (TextView) itemView.findViewById(R.id.foodName);
            pay = (TextView) itemView.findViewById(R.id.pay);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            //itemView.setOnClickListener((View.OnClickListener) this);



        }



    }



}
