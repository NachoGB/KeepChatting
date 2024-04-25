package com.example.user.keepchatting;

import android.content.Context;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class MessageSingleton {
    private MessageAdapter adaptador;
    private static MessageSingleton ourInstance;
    private FirebaseAuth auth;
    private final static String MESSAGE_CHILD="messages";
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference messageRef=db.collection("messages");
    private DatabaseReference messageReference;

    private MessageSingleton(Context context){

        Query query = messageRef.orderBy("now");
        FirestoreRecyclerOptions<Message> options =
                new FirestoreRecyclerOptions.Builder<Message>()
                        .setQuery(query,Message.class)
                        .build();

        adaptador=new MessageAdapter(context,messageReference,options);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        messageReference=database.getReference().child(MESSAGE_CHILD);
    }
    //Mètode que proporciona l'objecte únic de la mateixa classe
    public static MessageSingleton getInstance(Context context){
        if (ourInstance==null){
            ourInstance=new MessageSingleton(context);
        }
        return ourInstance;
    }
    //getter per a obtenir un atribut de la classe
    public MessageAdapter getAdaptador(){
        return adaptador;
    }

    public FirebaseAuth getAuth(){
        return auth;
    }

    public DatabaseReference getPuntsReference(){
        return messageReference;
    }
}