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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText edit_username,edit_name,edit_email,edit_password;
    Button btn_register;
    TextView txt_loginsayfasi;

    FirebaseAuth yetki;
    DatabaseReference yol;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_username=findViewById(R.id.edit_username);
        edit_name=findViewById(R.id.edit_name);
        edit_email=findViewById(R.id.edit_email);
        edit_password=findViewById(R.id.edit_password);
        btn_register=findViewById(R.id.btn_register_activity);
        txt_loginsayfasi=findViewById(R.id.txt_loginsayfasi);

        yetki=FirebaseAuth.getInstance();

        txt_loginsayfasi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pd=new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Lütfen Bekleyin..");
                pd.show();


                String str_username=edit_username.getText().toString();
                String str_name=edit_name.getText().toString();
                String str_email=edit_email.getText().toString();
                String str_password=edit_password.getText().toString();

                if(TextUtils.isEmpty(str_username)||TextUtils.isEmpty(str_name)|| TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(RegisterActivity.this, "Lütfen Tüm Alanları Doldurun!", Toast.LENGTH_SHORT).show();
                }
                else if(str_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Şifreniz minimum 6 karakter olmalı!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //yeni kullanıcı ekleme
                    save(str_username,str_name,str_email,str_password);
                    // pd.dismiss();
                }
            }
        });

    }

    private void save(final String username, final String name, String email, String password){
        yetki.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if(task.isSuccessful()){//veritabanın açma başarılı ise kullanici kaydetme işlemini yap.

                            FirebaseUser firebaseUser= yetki.getCurrentUser(); //yetkisi olan kullanıcıyı al.
                            String userId  =firebaseUser.getUid();
                            yol= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);//kullanıcının nereye gideceği// veritabanındaki yolu.
                            HashMap<String , Object> hashMap = new HashMap<>(); //birden fazla veriyi göndermek için hashmap kullanılıyoruz.
                            hashMap.put("id", userId);
                            hashMap.put("username", username.toLowerCase());
                            hashMap.put("name", name);
                            hashMap.put("bio", "");
                            hashMap.put("photoUrl", "https://firebasestorage.googleapis.com/v0/b/foodsmap-9dd9e.appspot.com/o/placeholder.jpg?alt=media&token=5209d8d7-5bce-4282-9353-165144ceb097");

                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task){

                                    //if(task.isSuccessful()){// eğer kullanıcı oluşturma işlemi başarılıysa
                                    pd.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this, Location_PermissionActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    // startActivity(new Intent(RegisterActivity.this, HomeActivity.class));

                                    // }
                                }
                            });
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Bu mail veya şifreyle kayıt başarısız", Toast.LENGTH_LONG).show();
                        }


                    }
                });
    }
}