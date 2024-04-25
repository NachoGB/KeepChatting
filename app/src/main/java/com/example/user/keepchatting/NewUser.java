package com.example.user.keepchatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NewUser  extends AppCompatActivity {
    private ImageView imageView;
    private EditText editTextName, editTextEmail, editTextPassword;
    private Button bSignUp;

    FirebaseAuth auth;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        auth=FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference();

        imageView = (ImageView)findViewById(R.id.logo_app);
        imageView.setImageResource(R.drawable.keepchatting);
        editTextName=(EditText)findViewById(R.id.nameSignUp);
        editTextEmail=(EditText)findViewById(R.id.emailSignUp);
        editTextPassword=(EditText)findViewById(R.id.passwordSignUp);
        bSignUp=(Button)findViewById(R.id.signUp);

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.NAME=editTextName.getText().toString();
                Constants.NAME=Constants.NAME.substring(0,1).toUpperCase()+Constants.NAME.substring(1);
                Constants.EMAIL=editTextEmail.getText().toString();
                Constants.PASSWORD=editTextPassword.getText().toString();

                if (!Constants.NAME.isEmpty() && !Constants.EMAIL.isEmpty() && !Constants.PASSWORD.isEmpty()){
                    if (Constants.PASSWORD.length()>=6){
                        registerUser();
                    }else {
                        Toast.makeText(NewUser.this,"La contraseña debe tener al menos 6 carácteres",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(NewUser.this,"Complete los campos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(){
        auth.createUserWithEmailAndPassword(Constants.EMAIL,Constants.PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> map=new HashMap<>();
                    map.put("name",Constants.NAME);
                    map.put("email",Constants.EMAIL);
                    map.put("password",Constants.PASSWORD);

                    String id=auth.getCurrentUser().getUid();

                    db.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(NewUser.this,ChatActivity.class));
                                finish();
                            }else {
                                Toast.makeText(NewUser.this,"No se pudieron crear los datos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(NewUser.this,"No se pudo registrar este usuario",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(NewUser.this,ProfileActivity.class));
            finish();
        }else {

        }
    }
}
