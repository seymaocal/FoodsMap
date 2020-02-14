package com.example.foodsmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodsmap.fragment.HomeFragment;
import com.example.foodsmap.fragment.ProfileFragment;
import com.example.foodsmap.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment choosefragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();
        if(intent !=null){
            String sender = intent.getString("sender");
            SharedPreferences.Editor  editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileid",sender);//profileId
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment,new ProfileFragment()).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment,new HomeFragment()).commit();
        }

        //anasayfa ilk açıldığında home sayfası açılsın.!!!
       // getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment,new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            choosefragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            choosefragment = null;
                            //startActivity(new Intent(HomeActivity.this, MainActivity.class));
                            choosefragment = new SearchFragment();
                            break;
                        case R.id.nav_profile:
                            // SharedPreferences.Editor editor= getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            // editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            // editor.apply();
                            //choosefragment = new ProfileFragment();
                            choosefragment = null;
                            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                            break;
                    }

                    if(choosefragment != null){//çerçevede birşey varsa çerçeveleri çağırır.
                        //geçişebaşla.yerdeğiştir.home_fragmnetin içine seçilen çerçeveyi yerleştirecek!!
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment,choosefragment).commit();
                    }
                    return true;
                }
            };

}