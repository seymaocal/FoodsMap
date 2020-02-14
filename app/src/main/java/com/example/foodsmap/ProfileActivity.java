package com.example.foodsmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodsmap.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment choosefragment = null;

    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences.Editor editor= getSharedPreferences("PREFS",MODE_PRIVATE).edit();
        editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        editor.apply();
        getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment,new ProfileFragment()).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, PostActivity.class));
            }
        });

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){

                        case R.id.nav_home:
                            choosefragment = null;
                            Restaurant();
                            break;
                        /*case R.id.nav_search:
                            choosefragment = null;
                            startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                            //choosefragment = new SearchFragment();
                            //getSupportFragmentManager().beginTransaction().replace(R.id.deneme_fragment,choosefragment).commit();
                            break;*/
                        case R.id.nav_act:
                            choosefragment = null;
                            startActivity(new Intent(ProfileActivity.this, MapActivity.class));
                            break;
                        case R.id.nav_profile:
                            choosefragment = null;
                            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                            break;
                    }


                    return true;
                }
            };

    public void Restaurant(){
        String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Konum").child(currentUser).child("ilce");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ilce=dataSnapshot.getValue(String.class);
                Intent intent=new Intent(ProfileActivity.this, Location_PermissionActivity.class);
                intent.putExtra("ilce", ilce);
                startActivity(intent);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
 /*  private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_add:
                            choosefragment = null;
                            startActivity(new Intent(ProfileActivity.this, PostActivity.class));
                            break;

                    }*/

                   /*if(choosefragment == null){//çerçevede birşey varsa çerçeveleri çağırır.
                        //geçişebaşla.yerdeğiştir.home_fragmnetin içine seçilen çerçeveyi yerleştirecek!!
                       getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment,new ProfileFragment()).commit();
                      // choosefragment = new ProfileFragment();
                    }*/
                  /* return true;
                }
            };*/

}
