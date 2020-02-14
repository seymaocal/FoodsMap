package com.example.foodsmap.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodsmap.R;
import com.example.foodsmap.fragment.PostDetailsFragment;
import com.example.foodsmap.model.Post;
import com.example.foodsmap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private Context context;
    private List<Post> mPosts;

    public PhotosAdapter(Context context, List mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.photos_object,viewGroup,false);
        return new PhotosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Post post = mPosts.get(i);
        Glide.with(context).load(post.getPostPhoto()).into(viewHolder.post_image);

        //post detayı
        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor =context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("postid",post.getPostId());
                editor.apply();

                final String kim=post.getSender();

                final String current=FirebaseAuth.getInstance().getCurrentUser().getUid();

                            if(kim.equals(current))
                           {
                                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment, new PostDetailsFragment()).commit();
                            }
                            else {
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new PostDetailsFragment()).commit();
                            }
                Toast.makeText(context, "Photossss", Toast.LENGTH_SHORT).show();
              //  ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment, new PostDetailsFragment()).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView post_image;
        public ViewHolder (@NonNull View itemView){
            super(itemView);

            post_image = itemView.findViewById(R.id.post_photo_photosObject);
        }
    }
}
