package com.example.foodsmap;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodsmap.Adapter.CommentAdapter;
import com.example.foodsmap.Adapter.RestaurantCommentAdapter;
import com.example.foodsmap.model.Comment;
import com.example.foodsmap.model.RestaurantComment;
import com.example.foodsmap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

  public  class RestaurantCommentActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private RestaurantCommentAdapter commentAdapter;
        private List<RestaurantComment> listComment;//yorum listesi

        EditText edit_comment_add;
        ImageView profile_image;
        TextView txt_send;

        String restId;//gönderi id
        String senderId;//gönderen id

        FirebaseUser currentUser;//mevcut kullanıcı.

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_restaurant_comment);

            Toolbar toolbar = findViewById(R.id.toolbar_restaurantcommentActivity);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("RestaurantYorumlar");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            recyclerView = findViewById(R.id.recyclerview_restaurantcommentActivity);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            listComment = new ArrayList<>();
            commentAdapter = new RestaurantCommentAdapter(this,listComment);
            recyclerView.setAdapter(commentAdapter);

            edit_comment_add = findViewById(R.id.edt_commentAdd_restaurantcommentActivity);
            profile_image = findViewById(R.id.profile_image_restaurantcommentActivity);
            txt_send = findViewById(R.id.txt_send_restaurantcommentActivity);
            currentUser = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent = getIntent();
            restId = intent.getStringExtra("restId");
            senderId = intent.getStringExtra("sender");

            txt_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(edit_comment_add.getText().toString().equals("")){
                        Toast.makeText(RestaurantCommentActivity.this, "Boş yorum gönderemezsiniz", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String str_edit_comment_add= edit_comment_add.getText().toString();
                        commentAdd(str_edit_comment_add);
                    }
                }
            });

            //metotları çağırdım
            picture_take();
            commentRead();

        }

        private void commentAdd(String edit_comment_add_) {

            DatabaseReference commentAddress = FirebaseDatabase.getInstance().getReference("RestoranYorumları").child(restId);
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("comment",edit_comment_add_);
            hashMap.put("sender",currentUser.getUid());

            commentAddress.push().setValue(hashMap);
            edit_comment_add.setText("");
        }

        private void picture_take(){//resim al
            DatabaseReference imageTakeAddress = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            imageTakeAddress.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Glide.with(getApplicationContext()).load(user.getphotoUrl()).into(profile_image);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        private void commentRead(){
            DatabaseReference commentReadAddress = FirebaseDatabase.getInstance().getReference("RestoranYorumları").child(restId);
            commentReadAddress.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listComment.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        RestaurantComment comment = snapshot.getValue(RestaurantComment.class);
                        listComment.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


