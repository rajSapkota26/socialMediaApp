package com.technoabinash.mig33.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.technoabinash.mig33.ChatDetailsActivity;
import com.technoabinash.mig33.R;
import com.technoabinash.mig33.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<User> users;
    Context context;
    boolean isFragment;
    FirebaseUser firebaseUser;
    int type;

    public UserAdapter(ArrayList<User> users, Context context, boolean isFragment ) {
        this.users = users;
        this.context = context;
        this.isFragment = isFragment;

    }

    public UserAdapter(ArrayList<User> users, Context context, boolean isFragment, int type) {
        this.users = users;
        this.context = context;
        this.isFragment = isFragment;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_view_item_on_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
              final User user = users.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (type!=2){
            holder.follow.setVisibility(View.VISIBLE);

        }
        if (type==2){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatDetailsActivity.class);
                    intent.putExtra("userName", user.getUsername());
                    intent.putExtra("userId", user.getId());
                    intent.putExtra("profile", user.getImageUrl());
                    context.startActivity(intent);
                }
            });
        }

        holder.uname.setText(user.getUsername());
        holder.fname.setText(user.getFullName());
        Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.avatar_icon).into(holder.profile);
        isFollowed(user.getId(),holder.follow);



        if (user.getId().equals(firebaseUser.getUid())){
            holder.follow.setVisibility(View.GONE);
        }
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.follow.getText().toString().equals(("follow"))){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                           .child("followers").child(firebaseUser.getUid()).setValue(true);
                    addNotification(user.getId());
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    private void isFollowed(String id, Button follow) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(id).exists()){
                    follow.setText("following");
                }else {
                    follow.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView uname, fname;
        Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_image);
            uname = itemView.findViewById(R.id.user_uname);
            fname = itemView.findViewById(R.id.user_fname);
            follow = itemView.findViewById(R.id.followBtn);
        }
    }
    private void addNotification(String userId) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", userId);
        map.put("text", "started following you.");
        map.put("postid", "");
        map.put("isPost", false);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }
}
