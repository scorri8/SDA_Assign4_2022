package com.example.sdaassign4_2022;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Images used are sourced from Public Domain Day 2019.
 * by Duke Law School's Center for the Study of the Public Domain
 * is licensed under a Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * A simple {@link Fragment} subclass.
 *
 * @author Chris Coughlan
 */
public class BookList extends Fragment {


    public BookList() {
        // Required empty public constructor
    }
    private DatabaseReference mDatabase;
    ViewPageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance("https://aissgn4stephencorri-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("books");
        RecyclerView recyclerView = view.findViewById(R.id.bookView_view);

        //add array for each item
        ArrayList<Book> bookList = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    bookList.add(data.getValue(Book.class));
                }
                LibraryViewAdapter recyclerViewAdapter = new LibraryViewAdapter(getContext(), bookList);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(postListener);

    }

}
