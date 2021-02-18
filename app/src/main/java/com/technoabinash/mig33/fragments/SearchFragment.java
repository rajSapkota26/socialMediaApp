package com.technoabinash.mig33.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.technoabinash.mig33.R;
import com.technoabinash.mig33.adapter.TagsAdapter;
import com.technoabinash.mig33.adapter.UserAdapter;
import com.technoabinash.mig33.model.User;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SocialAutoCompleteTextView searchBar;
    private ArrayList<User> mUser;
    private UserAdapter adapter;

    private TagsAdapter tagsAdapter;
    private ArrayList<String> mHashTags;
    private ArrayList<String> mNoOfHashTags;
    private RecyclerView tagrecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBar = view.findViewById(R.id.searchView);

        mUser = new ArrayList<>();

        adapter = new UserAdapter(mUser, getContext(), true);
        recyclerView.setAdapter(adapter);


        tagrecyclerView = view.findViewById(R.id.recyclerView_tags);
        tagrecyclerView.setHasFixedSize(true);
        tagrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mHashTags = new ArrayList<>();
        mNoOfHashTags = new ArrayList<>();

        tagsAdapter = new TagsAdapter(getContext(), mHashTags, mNoOfHashTags);
        tagrecyclerView.setAdapter(tagsAdapter);

        readUser();
        readHashTags();


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchHashTags(editable.toString());
            }
        });
        return view;
    }

    private void readHashTags() {
        FirebaseDatabase.getInstance().getReference().child("HashTag").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mHashTags.clear();
                mNoOfHashTags.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    mHashTags.add(dataSnapshot.getKey());
                    mNoOfHashTags.add(dataSnapshot.getChildrenCount()+"");
                }
                tagsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchHashTags(String s) {
         ArrayList<String> mSearchHashTags=new ArrayList<>();
         ArrayList<String> mSearchNoOfHashTags=new ArrayList<>();
         for (String s1:mHashTags){
             if (s1.toLowerCase().contains(s.toLowerCase())){
                 mSearchHashTags.add(s1);
                 mSearchNoOfHashTags.add(mNoOfHashTags.get(mHashTags.indexOf(s1)));

             }
             tagsAdapter.filterTags(mSearchHashTags,mSearchNoOfHashTags);
         }


    }

    private void readUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (TextUtils.isEmpty(searchBar.getText().toString())) {
                    mUser.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        mUser.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("username").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    mUser.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}