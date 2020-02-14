package com.example.foodsmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodsmap.Adapter.MenuAdapter;
import com.example.foodsmap.Adapter.MyCommisionAdapter;
import com.example.foodsmap.model.Menu;
import com.example.foodsmap.model.MyCommission;
import com.example.foodsmap.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyCommissionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyCommisionAdapter myCommissionAdapter;
    private List<MyCommission> myCommissionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commission);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_myCommisionActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        myCommissionList = new ArrayList<>();
        myCommissionAdapter = new MyCommisionAdapter(this, myCommissionList);
        recyclerView.setAdapter(myCommissionAdapter);
        String senderId= FirebaseAuth.getInstance().getUid();
        myCommissionRead();
    }



    private void myCommissionRead(){
        String currentUser= FirebaseAuth.getInstance().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference res_id=database.getReference().child("Siparislerim").child(currentUser);
        res_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String senderId= FirebaseAuth.getInstance().getUid();
                String id="";
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                    MyCommission myCommission = snapshot1.getValue(MyCommission.class);
                    id=myCommission.getSenderId();

                    if(senderId.equals(id)){
                    MyCommission commission=new MyCommission();
                    String a=myCommission.getFood();
                    commission.setFood(myCommission.getFood());
                    commission.setPay(myCommission.getPay());
                    commission.setRestaurant_name(myCommission.getRestaurant_name());
                    commission.setSender(myCommission.getSender());
                    myCommissionList.add(commission);

                    }
                }
                myCommissionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
