package com.example.user.keepchatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message,MessageAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Message> messageList;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private Context context;
    public static LinearLayout layoutSender;
    public static TextView senderMessageText,senderName;
    public static LinearLayout layoutReceiver;
    public static TextView receiverMessageText,receiverName;

    public MessageAdapter(Context context, DatabaseReference dbRef, FirestoreRecyclerOptions<Message> options){
        super(options);
        this.context=context;
        this.dbRef=dbRef;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView){
            super(itemView);
            layoutSender=(LinearLayout)itemView.findViewById(R.id.layout_bubble_sent);
            senderMessageText=(TextView) itemView.findViewById(R.id.textview_chat_sent);
            senderName=(TextView)itemView.findViewById(R.id.textview_name_sent);
            layoutReceiver=(LinearLayout)itemView.findViewById(R.id.layout_bubble_received);
            receiverMessageText=(TextView)itemView.findViewById(R.id.textview_chat_received);
            receiverName=(TextView)itemView.findViewById(R.id.textview_name_received);
        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = inflater.inflate(R.layout.list_item_chat,null);
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat,parent,false);
        auth=FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int i, Message model) {
        String messageSenderId=auth.getCurrentUser().getUid();
        if (messageSenderId.equals(model.getUserID())){
            layoutReceiver.setVisibility(View.INVISIBLE);
            layoutSender.setVisibility(View.VISIBLE);
            senderMessageText.setBackgroundResource(R.drawable.shape_bubble_sent);
            senderMessageText.setText(model.getMessageText());
            senderName.setText(model.getName());
        }else{
            layoutReceiver.setVisibility(View.VISIBLE);
            layoutSender.setVisibility(View.INVISIBLE);
            receiverMessageText.setBackgroundResource(R.drawable.shape_bubble_received);
            receiverMessageText.setText(model.getMessageText());
            receiverName.setText(model.getName());
        }

    }
}
