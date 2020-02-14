package com.example.foodsmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodsmap.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment choosefragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //anasayfa ilk açıldığında home sayfası açılsın.!!!
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment,new SearchFragment()).commit();
        Toast.makeText(this, "searchActivity", Toast.LENGTH_SHORT).show();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            //recyclerView.setVisibility(recyclerView.VISIBLE);
                            choosefragment = null;

                            startActivity(new Intent(SearchActivity.this, DenemeActivity.class));

                            break;
                        case R.id.nav_search:
                            choosefragment = null;
                            startActivity(new Intent(SearchActivity.this, SearchActivity.class));
                            //choosefragment = new SearchFragment();
                            //getSupportFragmentManager().beginTransaction().replace(R.id.deneme_fragment,choosefragment).commit();
                            break;
                        case R.id.nav_act:
                            choosefragment = null;
                            startActivity(new Intent(SearchActivity.this, MapActivity.class));
                            break;
                        case R.id.nav_profile:
                            choosefragment = null;
                            startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                            break;
                    }

                    return true;
                }
            };
}
