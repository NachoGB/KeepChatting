package com.example.user.keepchatting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity {
    private TextView textViewName, textViewEmail;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth=FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference();

        textViewName=(TextView) findViewById(R.id.textViewName);
        textViewEmail=(TextView) findViewById(R.id.textViewEmail);

        preferences=getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor=preferences.edit();

        getUserInfo();
    }

    private void getUserInfo(){
        String id=auth.getCurrentUser().getUid();
        db.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name=dataSnapshot.child("name").getValue().toString();
                    String email=dataSnapshot.child("email").getValue().toString();

                    textViewName.setText(name);
                    textViewEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void tancarSessio(View view){
        auth.signOut();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater infl=getMenuInflater();
        infl.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if (id==R.id.signOut){
            editor.clear();
            editor.commit();
            tancarSessio(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
