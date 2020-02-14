package com.example.foodsmap.fragment;


import android.content.Context;
import android.content.SharedPreferences;
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
public class PostDetailsFragment extends Fragment {

private RecyclerView recyclerView;
private List<Post> postList;
private PostAdapter  postAdapter;
String postId;

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences preferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postId=preferences.getString("postid","none");

        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_post_details, container, false);
         recyclerView=view.findViewById(R.id.recyclerview_postDetails);
         recyclerView.setHasFixedSize(true);
         LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(getContext());
         recyclerView.setLayoutManager(linearLayoutManager);

         postList=new ArrayList<>();
         postAdapter=new PostAdapter(getContext(),postList);
         recyclerView.setAdapter(postAdapter);

         postRead();
        return view;
    }

    private void postRead() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Gonderiler").child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                Post post =dataSnapshot.getValue(Post.class);
                postList.add(post);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
