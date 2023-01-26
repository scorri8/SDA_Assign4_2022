package com.example.sdaassign4_2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CheckOut extends AppCompatActivity {

    TextView mDisplaySummary, confirm,availability;
    Calendar mDateAndTime = Calendar.getInstance();
    private DatabaseReference mDatabase;
    private String bookId;
    private String bookName;
    private Button dateButton,orderButton;
    private String borrowerId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        //set the toolbar we have overridden
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookName = getIntent().getStringExtra("bookName");
        bookId = getIntent().getStringExtra("bookId");

        dateButton = findViewById(R.id.date);
        orderButton = findViewById(R.id.orderButton);
        mDisplaySummary = findViewById(R.id.orderSummary);
        confirm = findViewById(R.id.confirm);
        availability = findViewById(R.id.availability);

        SharedPreferences sharedPreferences = getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        borrowerId = sharedPreferences.getString("borrowerId","");
        userName = sharedPreferences.getString("borrowerName","");

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = df.format(c);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence returnDate = DateUtils.formatDateTime(CheckOut.this, mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
                sendOrdered(returnDate.toString(),currentDate,bookId,borrowerId);
            }
        });


        //find the summary textview
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ordered_book");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()<1){
                    availability.setText("Book is available");
                }
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    SendOrdered sendOrdered = data.getValue(SendOrdered.class);
                    if(sendOrdered.getBookId().equals(bookId)) {
                        try {
                            Date strDate = df.parse(sendOrdered.getReturnDate());
                            if (System.currentTimeMillis() < strDate.getTime()) {
                                orderButton.setEnabled(false);
                                dateButton.setEnabled(false);
                                availability.setText("Book will be available on this date: " + sendOrdered.getReturnDate());
                            } else {
                                availability.setText("Book is available");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                availability.setText("Book is available");
            }
        };
        mDatabase.addValueEventListener(postListener);



        confirm.setText("Check out " + bookName);
    }

    public void onDateClicked(View v) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateAndTimeDisplay();
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(CheckOut.this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();

    }

    public void sendOrdered(String returnDate, String orderDate, String bookId, String borrowerId) {
        SendOrdered sendOrdered = new SendOrdered(returnDate, orderDate, bookId, borrowerId);
        mDatabase.child(bookId).setValue(sendOrdered);
        mDisplaySummary.setText("Order booked successfully");
    }

    private void updateDateAndTimeDisplay() {
        //date time year
        CharSequence currentTime = DateUtils.formatDateTime(this, mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        CharSequence SelectedDate = DateUtils.formatDateTime(this, mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        String finalSummary = userName + "select" +SelectedDate + " current time is " + currentTime;
        mDisplaySummary.setText(finalSummary);
    }
}
