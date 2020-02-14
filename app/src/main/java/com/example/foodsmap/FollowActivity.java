package com.example.foodsmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsmap.Adapter.UserAdapter;
import com.example.foodsmap.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity {

    String id;
    String header;
    List<String> idList;

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        header=intent.getStringExtra("header");

        Toolbar toolbar=findViewById(R.id.toolbar_followActivity);
       setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        recyclerView=findViewById(R.id.recyclerview_followActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList=new ArrayList<>();
        userAdapter=new UserAdapter(this,userList,false);
        recyclerView.setAdapter(userAdapter);

        idList=new ArrayList<>();
        
        switch (header){
            
            case "Beğeniler":
                GetLike();
            break;
            case "Takipçiler":
                GetFollower();
            break;
            case "Takip Edilenler":
                GetFollowed();
                break;
        }
    }

    //takip edilenler
    private void GetFollowed() {
        DatabaseReference followedAddress = FirebaseDatabase.getInstance().getReference("Takip").child(id).child("TakipEdilenler");
        followedAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                userShow();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //takipçiler
    private void GetFollower() {
        DatabaseReference followerAddress = FirebaseDatabase.getInstance().getReference("Takip").child(id).child("Takipciler");
        followerAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());

                }
                userShow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //beğeniler
    private void GetLike() {

        DatabaseReference likeAddress = FirebaseDatabase.getInstance().getReference("Begeniler").child(id);
        likeAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                userShow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //kullanıcıları göstermek için
    private void userShow(){
        final DatabaseReference userAddress=FirebaseDatabase.getInstance().getReference("Users");
        userAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);
                    for(String id:idList){
                        if(user.getId().equals(id)){
                            userList.add(user);
                        }
                    }
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
