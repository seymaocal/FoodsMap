package com.example.foodsmap.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.foodsmap.Adapter.PhotosAdapter;
import com.example.foodsmap.FollowActivity;
import com.example.foodsmap.OptionsActivity;
import com.example.foodsmap.ProfileEditActivity;
import com.example.foodsmap.R;
import com.example.foodsmap.model.Post;
import com.example.foodsmap.model.Restaurant;
import com.example.foodsmap.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {




    ImageView image_options,profile_photo;
    TextView txt_post,txt_follower,txt_followed,txt_name,txt_bio,txt_username;
    Button btn_editProfile;
    ImageButton image_btn_myPhotos,image_btn_saved;

    RecyclerView  recyclerViewPhotos;
    PhotosAdapter photosAdapter;
    List<Post> postList;

    FirebaseUser current_user;
    String profileId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        current_user= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileid","none");
        Toast.makeText(getContext(), "Profile Fragment", Toast.LENGTH_SHORT).show();
        image_options = view.findViewById(R.id.imageDehaze_profileFrame);
        profile_photo = view.findViewById(R.id.profileImage_profileFrame);

        txt_post = view.findViewById(R.id.txtPost_profileFrame);
        txt_follower = view.findViewById(R.id.follower_profileFrame);
        txt_followed = view.findViewById(R.id.followed_profileFrame);
        txt_name = view.findViewById(R.id.txt_name_profileFrame);
        txt_bio = view.findViewById(R.id.txt_bio_profileFrame);
        txt_username = view.findViewById(R.id.txt_username_profileFrame);

        btn_editProfile = view.findViewById(R.id.btn_editProfile_profileFrame);

        image_btn_myPhotos = view.findViewById(R.id.imagebtn_my_photos_profileFrame);

        recyclerViewPhotos = view.findViewById(R.id.recyclerview_profileFrame);
        recyclerViewPhotos.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),2);//3'lü ızgara olacak.
        recyclerViewPhotos.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        photosAdapter = new PhotosAdapter(getContext(), postList);
        recyclerViewPhotos.setAdapter(photosAdapter);

        //metotları çağır..
        user_information();
        getFollowers();
        postCount();
        myPhotos();

        if(profileId.equals(current_user.getUid())){
            btn_editProfile.setText("Profili Düzenle");
        }
        else{
            follow_control();
            image_btn_saved.setVisibility(View.GONE);
        }

        btn_editProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String btn = btn_editProfile.getText().toString();
                if(btn.equals("Profili Düzenle")){
                    startActivity(new Intent(getContext(), ProfileEditActivity.class));
                }
                else if(btn.equals("Takip Et")){//takip etme işlemi
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(current_user.getUid()).child("TakipEdilenler").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profileId).child("Takipciler").child(current_user.getUid()).setValue(true);

                }
                else if(btn.equals("Takip Ediliyor")){//takipten çıkma işlemi
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(current_user.getUid()).child("TakipEdilenler").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profileId).child("Takipciler").child(current_user.getUid()).removeValue();
                }
            }
        });

        image_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), OptionsActivity.class);
                startActivity(intent);
            }
        });

        txt_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), FollowActivity.class);
                intent.putExtra("id",profileId);
                intent.putExtra("header","Takipçiler");
                startActivity(intent);
            }
        });
        txt_followed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), FollowActivity.class);
                intent.putExtra("id",profileId);
                intent.putExtra("header","Takip Edilenler");
                startActivity(intent);
            }
        });
        return view;

    }


    private void user_information(){//kullanıcı bilgisi
        DatabaseReference userAddress = FirebaseDatabase.getInstance().getReference("Users").child(profileId);
        userAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext() == null){
                    return;
                }

                User user = dataSnapshot.getValue(User.class);
                String url=user.getphotoUrl();
                Glide.with(getContext()).load(url).into(profile_photo);
                txt_username.setText(user.getUsername());
                txt_name.setText(user.getName());
                txt_bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void follow_control(){
        DatabaseReference followAddress = FirebaseDatabase.getInstance().getReference().child("Takip").child(current_user.getUid()).child("TakipEdilenler");
        followAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(profileId).exists()){
                    btn_editProfile.setText("Takip Ediliyor");
                }
                else{
                    btn_editProfile.setText("Takip Et");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getFollowers(){ //takipçileri al..
        //takipçi sayısını alır
        DatabaseReference followerAddress =FirebaseDatabase.getInstance().getReference().child("Takip").child(profileId).child("Takipciler");
        followerAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_follower.setText(""+dataSnapshot.getChildrenCount());//çocuk sayısı yani takipçi sayısını aldık..
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //takip edilen sayısını alır
        DatabaseReference followedAddress =FirebaseDatabase.getInstance().getReference().child("Takip").child(profileId).child("TakipEdilenler");
        followedAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_followed.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void postCount(){//gönderi sayısı al.

        DatabaseReference postAddress = FirebaseDatabase.getInstance().getReference("Gonderiler");
        postAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getSender().equals(profileId)){///////////!!!!!!!!!!!!
                        i++;
                    }
                }
                txt_post.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void myPhotos(){
        DatabaseReference photosAddress = FirebaseDatabase.getInstance().getReference("Gonderiler");
        photosAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getSender().equals(profileId)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);

                photosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
