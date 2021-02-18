package com.technoabinash.mig33.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.squareup.picasso.Picasso;
import com.technoabinash.mig33.CommentActivity;
import com.technoabinash.mig33.FollowersActivity;
import com.technoabinash.mig33.R;
import com.technoabinash.mig33.fragments.PostDetailFragment;
import com.technoabinash.mig33.fragments.ProfileFragment;
import com.technoabinash.mig33.model.Post;
import com.technoabinash.mig33.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Post> mPosts;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, ArrayList<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_view_sample, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        Picasso.get().load(post.getPostImage()).into(holder.postImage);
        holder.description.setText(post.getPostDescription());


        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPostPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageUrl().equals("default")) {
                    holder.imageProfile.setImageResource(R.drawable.avatar_icon);
                } else {
                    Picasso.get().load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
                }
                holder.username.setText(user.getUsername());
                holder.author.setText("Post By: "+user.getFullName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        isLiked(post.getPostId(), holder.like);
        noOfLikes(post.getPostId(), holder.noOfLikes);
        getComments(post.getPostId(), holder.noOfComments);
        isSaved(post.getPostId(), holder.save);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostId()).child(firebaseUser.getUid()).setValue(true);

                    addNotification(post.getPostId(), post.getPostPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostId()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPostPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new ProfileFragment()).commit();
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPostPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new ProfileFragment()).commit();
            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPostPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new ProfileFragment()).commit();
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostId()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostId()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, CommentActivity.class);
                intent1.putExtra("postId", post.getPostId());
                intent1.putExtra("authorId", post.getPostPublisher());
                mContext.startActivity(intent1);
            }
        });

        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("authorId", post.getPostPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostId()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new PostDetailFragment()).commit();
            }
        });

        holder.noOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id", post.getPostPublisher());
                intent.putExtra("title", "likes");
                mContext.startActivity(intent);
            }
        });
    }

    private void addNotification(String postId, String postPublisher) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid", postPublisher);
        map.put("text", "liked your post.");
        map.put("postid", postId);
        map.put("isPost", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }

    private void getComments(String postId, TextView text1) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text1.setText("View All " + dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isSaved(String postId, ImageView save1) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()) {
                    save1.setImageResource(R.drawable.save_icon_on);
                    save1.setTag("saved");
                } else {
                    save1.setImageResource(R.drawable.save_icon);
                    save1.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void noOfLikes(String postId, TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void isLiked(String postId, ImageView likeImage) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    likeImage.setImageResource(R.drawable.like_icon_on);
                    likeImage.setTag("liked");
                } else {
                    likeImage.setImageResource(R.drawable.like_icon_off);
                    likeImage.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;

        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        SocialTextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.user_ppImage);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like_btn);
            comment = itemView.findViewById(R.id.comment_btn);
            save = itemView.findViewById(R.id.save_btn);
            more = itemView.findViewById(R.id.more_menu);

            username = itemView.findViewById(R.id.user_tname);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            author = itemView.findViewById(R.id.author);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            description = itemView.findViewById(R.id.description);
        }
    }
}
