package com.technoabinash.mig33.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.technoabinash.mig33.R;
import com.technoabinash.mig33.fragments.PostDetailFragment;
import com.technoabinash.mig33.model.Post;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private ArrayList<Post> mPosts;
    private Context mContext;

    public PhotoAdapter(ArrayList<Post> myPhotoList, Context context) {
        this.mPosts = myPhotoList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sample_photo_collection_profile, parent, false);
        return  new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.avatar_icon).into(holder.postImage);

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostId()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, new PostDetailFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return  mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView postImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}
