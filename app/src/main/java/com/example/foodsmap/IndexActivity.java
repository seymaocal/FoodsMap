package com.example.foodsmap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class IndexActivity extends AppCompatActivity {
    Button btn_login;
    Button btn_register;
    FirebaseUser IndexUser;

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private RelativeLayout mylay;
    @Override
    protected void onStart() {
        super.onStart();

        IndexUser = FirebaseAuth.getInstance().getCurrentUser(); //eğer kullanıcı veritabanında varsa direkt anasayfya göndersin.
        if(IndexUser != null){
            startActivity(new Intent(IndexActivity.this, Location_PermissionActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //arkaplan resim değiştirme
        mylay = (RelativeLayout) findViewById(R.id.mylayout);
        Timer timer = new Timer();
        MyTimer mt = new MyTimer();
        timer.schedule(mt, 2000, 2000);


        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isServicesOK() && internetKontrol()) {
                        Toast.makeText(getApplicationContext(), "İnternet Bağlı!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(IndexActivity.this,LoginActivity.class);
                        startActivity(intent);
                       // startActivity(new Intent(IndexActivity.this, LoginActivity.class));

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "İnternet Yok!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this,RegisterActivity.class));
            }
        });
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(IndexActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(IndexActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    protected boolean internetKontrol() { //interneti kontrol eden method
        // TODO Auto-generated method stub
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    class MyTimer extends TimerTask{
        public void run(){
            runOnUiThread(new Runnable() {

                Random rand = new Random();
                @Override
                public void run() {
                    int Images[] = {R.drawable.food1, R.drawable.food2,R.drawable.food3,R.drawable.food4,R.drawable.food5,R.drawable.food6,R.drawable.food7
                            ,R.drawable.food8,R.drawable.food9};
                    mylay.setBackgroundResource(Images[getRandomNumber()]);

                }
                private int getRandomNumber(){
                    return new Random().nextInt(9);
                }
            });
        }
    }

}