package com.example.foodsmap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodsmap.HomeActivity;
import com.example.foodsmap.R;
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

import java.util.List;

public class RestaurantCommentAdapter extends RecyclerView.Adapter<RestaurantCommentAdapter.ViewHolder>  {

    private Context mcontext;
    private List<RestaurantComment> mcommentList;

    FirebaseUser currentUser;

    public RestaurantCommentAdapter(Context mcontext, List<RestaurantComment> mcommentList) {
        this.mcontext = mcontext;
        this.mcommentList = mcommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.restaurant_comment_object,viewGroup,false);

        return new RestaurantCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final RestaurantComment comment = mcommentList.get(position);//

        viewHolder.txt_comment.setText(comment.getComment());

        getUserİnformation(viewHolder.profile_image,viewHolder.txt_username,comment.getSender());

        viewHolder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, HomeActivity.class);
                intent.putExtra("sender",comment.getSender());
                mcontext.startActivity(intent);

            }
        });

        viewHolder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, HomeActivity.class);
                intent.putExtra("sender",comment.getSender());
                mcontext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mcommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_image;
        public TextView txt_username,txt_comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profileImage_commentObject);
            txt_username = itemView.findViewById(R.id.txt_username_commentObject);
            txt_comment = itemView.findViewById(R.id.txt_comment_commentObject);

        }
    }

    //kullnıcı bilgisi alma metodu
    private void getUserİnformation(final ImageView imageView, final TextView username, String postId){
        DatabaseReference postIdAddress = FirebaseDatabase.getInstance().getReference().child("Users").child(postId);
        postIdAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Glide.with(mcontext).load(user.getphotoUrl()).into(imageView);
                username.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
