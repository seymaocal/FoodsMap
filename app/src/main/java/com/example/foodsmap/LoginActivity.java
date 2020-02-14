package com.example.foodsmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText edit_email_login, edit_password_login;
    Button btn_login_activity;
    TextView txt_registersayfasi;
    FirebaseAuth login_yetkisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email_login=findViewById(R.id.edit_email_login);
        edit_password_login=findViewById(R.id.edit_password_login);
        btn_login_activity=findViewById(R.id.btn_login);
        txt_registersayfasi=findViewById(R.id.txt_registersayfasi);

        login_yetkisi= FirebaseAuth.getInstance();
        txt_registersayfasi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btn_login_activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final ProgressDialog pdlogin=new ProgressDialog(LoginActivity.this);
                pdlogin.setMessage("Giriş Yapılıyor..");
                pdlogin.show();

                String str_emailLogin= edit_email_login.getText().toString();
                String str_passwordLogin= edit_password_login.getText().toString();

                if(TextUtils.isEmpty(str_emailLogin) || TextUtils.isEmpty(str_passwordLogin)){
                    Toast.makeText(LoginActivity.this, "Tüm alanları doldurun!!!", Toast.LENGTH_LONG).show();
                    pdlogin.dismiss();
                }
                else{
                    login_yetkisi.signInWithEmailAndPassword(str_emailLogin,str_passwordLogin)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task){
                                    if(task.isSuccessful()){
                                        DatabaseReference yolLogin = FirebaseDatabase.getInstance().getReference().child("Users").child(login_yetkisi.getCurrentUser().getUid());
                                        yolLogin.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pdlogin.dismiss();
                                                Intent intent = new Intent(LoginActivity.this, Location_PermissionActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            /* Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                             startActivity(intent);*/
                                                //finish(); //anasayfaya geçtikten sonra geri tuşuna basıldığında logine geri dönmesin diye finis yaptık.!!!
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                pdlogin.dismiss();
                                                Toast.makeText(LoginActivity.this, databaseError+"Giriş yapılamadı!!!", Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }
                                    else{
                                        pdlogin.dismiss();
                                        Toast.makeText(LoginActivity.this, "Giriş yapılamadı!!!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}