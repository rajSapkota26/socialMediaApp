package com.technoabinash.mig33;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technoabinash.mig33.adapter.UserAdapter;
import com.technoabinash.mig33.model.User;

import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity {

    private String id;
    private String title;
    private ArrayList<String> idList;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> mUsers;
    ImageView toolbar;
    private FirebaseUser fUser;

    String profileId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        getSupportActionBar().hide();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");

        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }



        id = profileId;
//        title = intent.getStringExtra("title");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatsActivity.super.onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(mUsers, this, false,2);
        recyclerView.setAdapter(userAdapter);

        idList = new ArrayList<>();

        getFollowings();
    }



    private void getFollowings() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    idList.add((snapshot.getKey()));
                }

                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showUsers() {

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (String id : idList) {
                        if (user.getId().equals(id)) {
                            mUsers.add(user);
                        }
                    }
                }
                Log.d("list f", mUsers.toString());
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}