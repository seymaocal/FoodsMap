package com.example.foodsmap.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsmap.Adapter.PostAdapter;
import com.example.foodsmap.R;
import com.example.foodsmap.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private List<String> followList;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        //bu kısım veritabaında postları alıpekrana vermek için yazılan kodlardan bazıları..
        recyclerView = view.findViewById(R.id.recyclerview_homeFragment);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        followControl();//burasını kapatacaktın hata verirse eğer.

        return view;
    }

    private  void followControl(){

        followList = new ArrayList<>();
        DatabaseReference followAddress = FirebaseDatabase.getInstance().getReference("Takip").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("TakipEdilenler");


        followAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followList.add(snapshot.getKey());

                }

                postRead();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postRead(){
        DatabaseReference postAddress = FirebaseDatabase.getInstance().getReference("Gonderiler");
        postAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for(String id: followList){
                        if(post.getSender().equals(id)){/////////!!!!!!!!!!!!!!!!!1post.getSender().equals(id)
                            postList.add(post);
                        }
                    }

                }
                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
