package com.example.foodsmap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.foodsmap.model.Rating;
import com.example.foodsmap.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Restaurant_DetailsActivity extends AppCompatActivity{
    String currentUser;
    TextView restaurant_name,restaurant_address, siparis,restaurant_comment,restaurant_website,restaurant_number;
    ImageView imageView,restaurant_save;
    Bitmap bitmap;
    private RatingBar ratingBar;
    private TextView ratingDegeri;
    private TextView gonderButton;

    private List<Rating> listComment;
    final String[] a = {""};
    final String[] h={""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant__details);

        restaurant_name=findViewById(R.id.restaurant_name);
        restaurant_address=findViewById(R.id.restaurant_address);
        restaurant_website=findViewById(R.id.restaurant_website);
        restaurant_number=findViewById(R.id.restaurant_number);
        imageView=findViewById(R.id.image);
        restaurant_comment=findViewById(R.id.restaurant_comment);
        siparis=findViewById(R.id.siparis);
        restaurant_save=findViewById(R.id.restaurant_save);

        ratingDegeri = (TextView) findViewById(R.id.sonucCiktiTV);

        addListenerOnRatingBar();
        addListenerOnButton();

        Bundle intent =getIntent().getExtras();
        final int positionn = intent.getInt("position_");
        final String name = intent.getString("name");
        final String position=Integer.toString(positionn);

        final String[] image_url = {""};
        //new DownloadImageTask(imageView).execute(imageUrl);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference res_id=database.getReference().child("restaurant");
        res_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String restaurantName="";


                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot1.getValue(Restaurant.class);

                    restaurantName=restaurant.getName();
                    if(restaurantName.equals(name))
                    {
                        restaurant_name.setText(restaurant.getName());
                        restaurant_address.setText(restaurant.getAddress());
                        restaurant_number.setText(restaurant.getPhone());
                        restaurant_website.setText(restaurant.getWeb_site());

                        Restaurant pd =new Restaurant();
                        a[0]=restaurant.getName();

                        image_url[0] =restaurant.getImage_url();
                        Glide.with(Restaurant_DetailsActivity.this).load(image_url[0]).into(imageView);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        siparis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Restaurant_DetailsActivity.this,MenuActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        restaurant_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Restaurant_DetailsActivity.this,RestaurantCommentActivity.class);
                intent.putExtra("restId", a[0]);
                intent.putExtra("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);

            }
        });
        currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final Restaurant restaurant =new Restaurant();
        restaurant_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference restaurant_save=FirebaseDatabase.getInstance().getReference("KaydedilenRestoranlar").child(currentUser);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name", restaurant_name.getText());
                hashMap.put("address", restaurant_address.getText());
                hashMap.put("image_url",image_url[0]);
                restaurant_save.push().setValue(hashMap);
            }
        });

        Toast.makeText(this, h[0], Toast.LENGTH_SHORT).show();
        listComment=new ArrayList<>();
        DatabaseReference commentReadAddress = FirebaseDatabase.getInstance().getReference("Rating").child(name);
        commentReadAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float ratin_value=0; float Count = 0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Rating rating = snapshot.getValue(Rating.class);

                     Count++;
                     ratin_value+=rating.getRating();
                    //listComment.add(comment);
                }
                float sonuc=(ratin_value/Count);
               // deger(sonuc);
                ratingDegeri.setText(String.valueOf(sonuc));
                ratingBar.setRating(sonuc);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public float deger(float sonuc){
        return sonuc;
    }
    public void addListenerOnRatingBar() {

      // final String[] rating_value={""};
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingDegeri = (TextView) findViewById(R.id.sonucCiktiTV);
        LayerDrawable stars=(LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        //ratingBar.setNumStars(3);

        //ratingDegeri.setText(String.valueOf(rating));

        //Rating değiştiği anda bunu TextView'da görüntüle
      /*  ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
               // ratingDegeri.setText(String.valueOf(rating));
                //rating_value[0]=String.valueOf(rating);

            }
        });*/


    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        gonderButton = (TextView) findViewById(R.id.gonderButton);


        gonderButton.setOnClickListener(new View.OnClickListener() {

            final int[] kontrol=new int[1];
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                final String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
               final DatabaseReference ratingAddress2 = FirebaseDatabase.getInstance().getReference("Rating").child(a[0]);
                ratingAddress2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Rating rating = snapshot.getValue(Rating.class);
                            if(currentUser==rating.getSender()){

                                Toast.makeText(Restaurant_DetailsActivity.this,
                                       "oy var",
                                        1).show();
                                kontrol[0]=1;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(kontrol[0]!=1) {
                    DatabaseReference ratingAddress = FirebaseDatabase.getInstance().getReference("Rating").child(a[0]);
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("rating", ratingBar.getRating());
                    hashMap.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ratingAddress.push().setValue(hashMap);
                    Toast.makeText(Restaurant_DetailsActivity.this,
                            " oy yok",
                            1).show();
                }
               /*DatabaseReference ratingAddress = FirebaseDatabase.getInstance().getReference("Rating").child(a[0]);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("rating", ratingBar.getRating());
                hashMap.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                ratingAddress.push().setValue(hashMap);
                //Buttona tıklandığında o anki rating değerini içeren bir toast mesajı göster
                Toast.makeText(Restaurant_DetailsActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        1).show();*/

            }
        });
    }

       /* public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            bitmap=null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            onPostExecute(bitmap);
            bmImage.setImageBitmap(bitmap);
        }
    }*/

}
