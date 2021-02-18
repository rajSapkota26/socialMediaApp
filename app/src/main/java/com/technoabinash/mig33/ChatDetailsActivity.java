package com.technoabinash.mig33;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;
import com.technoabinash.mig33.adapter.ChatAdapter;
import com.technoabinash.mig33.model.MessageModel;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailsActivity extends AppCompatActivity {
    //declare variable
    FirebaseAuth mAuth;
    FirebaseDatabase database;


    String userName;
    String profile;

    CircleImageView profileImage, btnMsg;
    TextView chatUserName;
    ImageView backToChat, mAudioCallButton, mVideoCallButton;
    RecyclerView chatRecyclerView;
    EditText txMessage;
    // for call


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        getSupportActionBar().hide();
        // initialize variable
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String senderId = mAuth.getUid();


        String receiveId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        profile = getIntent().getStringExtra("profile");


        profileImage = findViewById(R.id.userProfile);
        chatUserName = findViewById(R.id.chatUserName);
        btnMsg = findViewById(R.id.btnMsgSend);
        backToChat = findViewById(R.id.btnBackToChat);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        txMessage = findViewById(R.id.etxMessage);
        mAudioCallButton = findViewById(R.id.msgAudioCall);
        mVideoCallButton = findViewById(R.id.msgVideoCall);


        Picasso.get().load(profile).placeholder(R.drawable.user_icon).into(profileImage);
        chatUserName.setText(userName);

        //for call



        mVideoCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         startActivity(new Intent(ChatDetailsActivity.this,VideoConference.class));

            }
        });



        backToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatDetailsActivity.super.onBackPressed();
            }
        });

        final String senderRoom = senderId + receiveId;
        final String receiverRoom = receiveId + senderId;

        final ArrayList<MessageModel> messagesModels = new ArrayList<>();
        final ChatAdapter adapter = new ChatAdapter(messagesModels, this, receiveId);
        chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel sms = dataSnapshot.getValue(MessageModel.class);
                    sms.setMessageId(snapshot.getKey());

                    messagesModels.add(sms);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txMessage.getText().toString().isEmpty()) {
                    txMessage.setHint("Enter your message");
                    return;
                }
                String message = txMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimeStamp(new Date().getTime());
                txMessage.setText("");
                database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });


        //audio call


    }


}