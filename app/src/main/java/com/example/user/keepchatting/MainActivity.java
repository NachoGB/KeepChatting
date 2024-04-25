package com.example.user.keepchatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText editTextEmail,editTextPassword;
    private Button bLogIn,bcreateUser;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();

        imageView = (ImageView)findViewById(R.id.logo_app);
        imageView.setImageResource(R.drawable.keepchatting);
        editTextEmail=(EditText)findViewById(R.id.emailLogIn);
        editTextPassword=(EditText)findViewById(R.id.passwordLogIn);
        bLogIn=(Button)findViewById(R.id.logIn);
        bcreateUser=(Button)findViewById(R.id.createUser);

        bLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Constants.EMAIL=editTextEmail.getText().toString();
            Constants.PASSWORD=editTextPassword.getText().toString();

            if (!Constants.EMAIL.isEmpty() && !Constants.PASSWORD.isEmpty()){
                loginUser();
            }else {
                Toast.makeText(MainActivity.this,"Complete los campos",
                        Toast.LENGTH_SHORT).show();
            }
            }
        });

        bcreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llancarCrearUsuario(null);
            }
        });

        preferences=getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor=preferences.edit();

        if (preferences.contains("email")){
            Intent intent=new Intent(this,ChatActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void saveData(View view){
        editor.putString("email",Constants.EMAIL);
        editor.putString("password",Constants.PASSWORD);
        editor.commit();
    }

    private void loginUser(){
        auth.signInWithEmailAndPassword(Constants.EMAIL,Constants.PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()){
                saveData(null);
                startActivity(new Intent(MainActivity.this,ChatActivity.class));
                finish();
            }else {
                Toast.makeText(MainActivity.this,"No se pudo iniciar sesión, comprueba los datos",
                        Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    public void llancarCrearUsuario(View view){
        Intent i=new Intent(this, NewUser.class);
        startActivity(i);
    }
}
