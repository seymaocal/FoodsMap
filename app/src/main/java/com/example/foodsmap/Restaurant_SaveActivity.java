package com.example.foodsmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsmap.Adapter.RestaurantAdapter;
import com.example.foodsmap.Adapter.Restaurant_SaveAdapter;
import com.example.foodsmap.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Restaurant_SaveActivity extends AppCompatActivity  implements Restaurant_SaveAdapter.OnRestaurantListener {
    private RecyclerView recyclerView;
    private Restaurant_SaveAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant__save);

        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        restaurantList = new ArrayList<>();
        restaurantAdapter = new Restaurant_SaveAdapter(this, restaurantList,this);
        recyclerView.setAdapter(restaurantAdapter);
        restaurantRead();
    }

    private void restaurantRead(){
        final String[] address = {""};
        final String[] name = {""};
       String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference a=database.getReference().child("KaydedilenRestoranlar").child(currentUser);
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menuList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant menu = snapshot.getValue(Restaurant.class);
                    name[0] = menu.getName();
                    address[0] = menu.getAddress();

                    Restaurant restaurantt = new Restaurant();
                    restaurantt.setName(menu.getName());
                    restaurantt.setAddress(menu.getAddress());
                    restaurantt.setImage_url(menu.getImage_url());
                    restaurantList.add(restaurantt);
                }
                restaurantAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRestaurantClick(int position) {
        Intent ıntent=new Intent(Restaurant_SaveActivity.this, Restaurant_DetailsActivity.class);
        ıntent.putExtra("position_", position);
        String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productName)).getText().toString();
        ıntent.putExtra("name", name);
        startActivity(ıntent);
    }
}
