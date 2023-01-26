package com.example.sdaassign4_2022;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEdit;
    private EditText userName;
    private EditText borrowerID;
    private EditText email;
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        sharedPreferencesEdit = sharedPreferences.edit();
        userName = view.findViewById(R.id.userName);
        borrowerID = view.findViewById(R.id.borrowerID);
        email = view.findViewById(R.id.email);

        String borrowerName = sharedPreferences.getString("borrowerName","");
        String borrowerIdNumber = sharedPreferences.getString("borrowerId","");
        String emailId = sharedPreferences.getString("email","");

        userName.setText(borrowerName);
        borrowerID.setText(borrowerIdNumber);
        email.setText(emailId);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInPreference(v);
            }
        });

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPreference(v);
            }
        });
    }

    public void resetPreference(View view) {
        sharedPreferencesEdit.clear();
        sharedPreferencesEdit.commit();
        userName.setText("");
        borrowerID.setText("");
        email.setText("");
        Toast.makeText(requireActivity(), "Detail reset.", Toast.LENGTH_SHORT).show();
    }

    public void saveInPreference(View view) {
        String borrowerName = userName.getText().toString();
        String borrowerIdNumber = borrowerID.getText().toString();
        String emailId = email.getText().toString();

        if(emailId.contains("@")){
            sharedPreferencesEdit.putString("borrowerName",borrowerName);
            sharedPreferencesEdit.putString("email",emailId);
            sharedPreferencesEdit.putString("borrowerId",borrowerIdNumber);
            sharedPreferencesEdit.commit();
            Toast.makeText(requireActivity(), "Detail saved successfully.", Toast.LENGTH_SHORT).show();
        }else{
            email.requestFocus();
            Toast.makeText(requireActivity(), "Please correct your email address.", Toast.LENGTH_SHORT).show();
        }
    }

}
