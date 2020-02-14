package com.example.foodsmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class OptionsActivity extends AppCompatActivity {

    TextView txt_exit, text_search,txt_commission,txt_restaurantSave_optionsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        text_search=findViewById(R.id.txt_search_optionsActivity);
        txt_exit=findViewById(R.id.txt_exit_optionsActivity);
        txt_commission=findViewById(R.id.txt_mysiparis_optionsActivity);
        txt_restaurantSave_optionsActivity=findViewById(R.id.txt_restaurantSave_optionsActivity);

        //Toollbar ayarları
        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar_optionsActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menü");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OptionsActivity.this,IndexActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        txt_commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OptionsActivity.this,MyCommissionActivity.class);
                startActivity(intent);
            }
        });
        text_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OptionsActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        txt_restaurantSave_optionsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OptionsActivity.this,Restaurant_SaveActivity.class);
                startActivity(intent);
            }
        });
    }

}
