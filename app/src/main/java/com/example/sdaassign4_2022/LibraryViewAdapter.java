package com.example.sdaassign4_2022;

        /*
         * Copyright (C) 2016 The Android Open Source Project
         *
         * Licensed under the Apache License, Version 2.0 (the "License");
         * you may not use this file except in compliance with the License.
         * You may obtain a copy of the License at
         *
         *      http://www.apache.org/licenses/LICENSE-2.0
         *
         * Unless required by applicable law or agreed to in writing, software
         * distributed under the License is distributed on an "AS IS" BASIS,
         * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         * See the License for the specific language governing permissions and
         * limitations under the License.
         */

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

        import java.util.ArrayList;
        import java.util.ResourceBundle;


/*
 * @author Chris Coughlan 2019
 */
public class LibraryViewAdapter extends RecyclerView.Adapter<LibraryViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mNewContext;

    private ArrayList<Book> bookArrayList;

    LibraryViewAdapter(Context mNewContext, ArrayList<Book> bookArrayList) {
        this.mNewContext = mNewContext;
        this.bookArrayList = bookArrayList;
    }

    //declare methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: was called");

        viewHolder.authorText.setText(bookArrayList.get(position).getAuthor());
        viewHolder.titleText.setText(bookArrayList.get(position).getTitle());

        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        StorageReference imageRef = storageReference.getReferenceFromUrl(bookArrayList.get(position).getImage());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mNewContext)
                        .load(uri)
                        .into( viewHolder.imageItem);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mNewContext, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //should check here to see if the book is available.
        viewHolder.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                SharedPreferences sharedPreferences = mNewContext.getSharedPreferences("user_detail", Context.MODE_PRIVATE);
                String borrowerName = sharedPreferences.getString("borrowerName","");
                if(borrowerName.isEmpty()){
                    Toast.makeText(mNewContext, "Please update your setting before checkout.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mNewContext, bookArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent myOrder = new Intent(mNewContext, CheckOut.class);
                    myOrder.putExtra("bookId",position+"");
                    myOrder.putExtra("bookName",bookArrayList.get(position).getTitle());
                    mNewContext.startActivity(myOrder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    //view holder class for recycler_list_item.xml
    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageItem;
        TextView authorText;
        TextView titleText;
        Button checkOut;
        RelativeLayout itemParentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            //grab the image, the text and the layout id's
            imageItem = itemView.findViewById(R.id.bookImage);
            authorText = itemView.findViewById(R.id.authorText);
            titleText = itemView.findViewById(R.id.bookTitle);
            checkOut = itemView.findViewById(R.id.out_button);
            itemParentLayout = itemView.findViewById(R.id.listItemLayout);

        }
    }
}
