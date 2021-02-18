package com.technoabinash.mig33.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technoabinash.mig33.R;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> mtags;
    private ArrayList<String> noOfTags;

    public TagsAdapter(Context context, ArrayList<String> mtags, ArrayList<String> noOfTags) {
        this.context = context;
        this.mtags = mtags;
        this.noOfTags = noOfTags;
    }

    @NonNull
    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tags_view_itemon_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsAdapter.ViewHolder holder, int position) {
        holder.tag.setText("# "+mtags.get(position));
        holder.noOfpost.setText(noOfTags.get(position)+" posts");
    }

    @Override
    public int getItemCount() {
        return mtags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag, noOfpost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.hash_tag);
            noOfpost = itemView.findViewById(R.id.no_post);
        }
    }
    public  void  filterTags(ArrayList<String> tags, ArrayList<String> mNoOfTags){
        this.mtags=tags;
        this.noOfTags=mNoOfTags;
    }
}
