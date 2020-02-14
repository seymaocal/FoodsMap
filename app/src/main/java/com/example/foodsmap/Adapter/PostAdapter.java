package com.example.foodsmap.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodsmap.CommentsActivity;
import com.example.foodsmap.FollowActivity;
import com.example.foodsmap.R;
import com.example.foodsmap.fragment.PostDetailsFragment;
import com.example.foodsmap.fragment.ProfileFragment;
import com.example.foodsmap.model.Post;
import com.example.foodsmap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    public Context mContext;
    public List<Post> mPost;
    private FirebaseUser currentFirebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.post_object, viewGroup,false);
        return  new  PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPost.get(i);
        Glide.with(mContext).load(post.getPostPhoto()).into(viewHolder.post_photo);
        if(post.getPostContent().equals("")){
            viewHolder.txt_post_context.setVisibility(View.GONE);
        }
        else{
            viewHolder.txt_post_context.setVisibility(View.VISIBLE);
            viewHolder.txt_post_context.setText(post.getPostContent());

        }

        sender_information(viewHolder.profile_photo,viewHolder.txt_username,viewHolder.txt_sender,post.getSender());
        likes(post.getPostId(),viewHolder.like_photo);
        likeCount(viewHolder.txt_like,post.getPostId());
        getComment(post.getPostId(),viewHolder.txt_comment);

        viewHolder.like_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.like_photo.getTag().equals("beğen")){
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(post.getPostId()).child(currentFirebaseUser.getUid()).setValue(true);
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(post.getPostId()).child(currentFirebaseUser.getUid()).removeValue();

                }
            }
        });

        viewHolder.comment_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("sender",post.getSender());
                mContext.startActivity(intent);
            }
        });

        viewHolder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postId",post.getPostId());
                intent.putExtra("sender",post.getSender());
                mContext.startActivity(intent);
            }
        });

        ///
        viewHolder.profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor =mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getSender());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new ProfileFragment()).commit();
            }
        });

        viewHolder.txt_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor =mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getSender());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new ProfileFragment()).commit();
            }
        });

        viewHolder.txt_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor =mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getSender());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new ProfileFragment()).commit();
            }
        });

        //post detayı
        viewHolder.post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor =mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getPostId());
                editor.apply();

                Toast.makeText(mContext, "PostAdapter", Toast.LENGTH_SHORT).show();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new PostDetailsFragment()).commit();
            }
        });
////

        //beğeniler textine basınca kimlerin beğendiğinin listesi
        viewHolder.txt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, FollowActivity.class);
                intent.putExtra("id",post.getPostId());
                intent.putExtra("header","Beğeniler");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {

        public ImageView profile_photo, post_photo,like_photo,comment_photo,saved_photo;
        public TextView txt_username,txt_like,txt_sender,txt_post_context,txt_comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            profile_photo = itemView.findViewById(R.id.profile_photo_object);
            post_photo = itemView.findViewById(R.id.post_image_user_object);
            like_photo = itemView.findViewById(R.id.like_post_object);
            comment_photo = itemView.findViewById(R.id.comment);
            saved_photo = itemView.findViewById(R.id.saved_post_object);

            txt_username = itemView.findViewById(R.id.txt_username_object);
            txt_like = itemView.findViewById(R.id.txt_like_post_object);
            txt_sender = itemView.findViewById(R.id.txt_sender_post_object);
            txt_post_context = itemView.findViewById(R.id.txt_context_post_object);
            txt_comment = itemView.findViewById(R.id.txt_comment_post_object);

        }
    }

    private void getComment(String postId, final TextView comment){//yorumları veritabanından al

        DatabaseReference getCommentAddress = FirebaseDatabase.getInstance().getReference("Yorumlar").child(postId);
        getCommentAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment.setText(dataSnapshot.getChildrenCount()+" yorumun hepsini gör");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void likes(String postId, final ImageView imageView){
        final FirebaseUser currentUser  = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference likeAddress = FirebaseDatabase.getInstance().getReference().child("Begeniler").child(postId);
        likeAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(currentUser.getUid()).exists()){//datasnapshot içinde mevcut kullanıcımı uidisi varsa
                    imageView.setImageResource(R.drawable.icon_like_red);
                    imageView.setTag("beğenildi");
                }
                else{
                    imageView.setImageResource(R.drawable.icon_like);
                    imageView.setTag("beğen");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void likeCount(final TextView likes,String postId){
        DatabaseReference likeCountAddress = FirebaseDatabase.getInstance().getReference().child("Begeniler").child(postId);
        likeCountAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" beğeni");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sender_information(final ImageView profile_photo, final TextView username, final TextView sender, String userId){//gönderen bilgisi
        DatabaseReference dataAddress = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        dataAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getphotoUrl()).into(profile_photo);
                username.setText(user.getUsername());
                sender.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
