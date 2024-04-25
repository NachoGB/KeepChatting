package com.example.user.keepchatting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private DatabaseReference dbRef;
    private EditText messageText;
    private ImageView buttonSend;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MessageAdapter adapter;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String name="";

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        auth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference();
        messageText=(EditText)findViewById(R.id.edittext_chat);
        buttonSend=(ImageView)findViewById(R.id.button_send);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        preferences=getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor=preferences.edit();

        adapter=MessageSingleton.getInstance(this).getAdaptador();

        recyclerView = (RecyclerView) findViewById(R.id.list_chat);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void sendMessage(){
        //First get the name
        dbRef.child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    name=dataSnapshot.child("name").getValue().toString();

                    //Then send the message
                    db.collection("messages")
                        .add(new Message(auth.getUid(),messageText.getText().toString(),name))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("", "Message with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("", "Error adding message", e);
                            }
                        });
                    messageText.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater infl=getMenuInflater();
        infl.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void llancarPerfil(View view){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void tancarSessio(View view){
        auth.signOut();
        startActivity(new Intent(ChatActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if (id==R.id.perfil){
            llancarPerfil(null);
            return true;
        }
        if (id==R.id.signOut){
            editor.clear();
            editor.commit();
            tancarSessio(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
