package com.example.mediamagictaskapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediamagictaskapp.Model.Author;
import com.example.mediamagictaskapp.R;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;

    List<Author> authorList = Collections.emptyList();
    Author author;
    int currntPos = 0;

    public RecyclerViewAdapter(Context context, List<Author> authorList)
    {
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.authorList = authorList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.list_items, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Author author = authorList.get(position);

        myHolder.txtAuthorName.setText(author.author_name);

        // load image into imageview using glide
        Glide.with(context).load("https://picsum.photos/300/300?image=" + author.id)
                .error(R.drawable.img_error)
                .into(myHolder.imgAuthorImg);
    }

    @Override
    public int getItemCount()
    {
        return authorList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView imgAuthorImg;
        TextView txtAuthorName;

        public MyHolder(View itemView)
        {
            super(itemView);

            imgAuthorImg = itemView.findViewById(R.id.imgAuthorImg);
            txtAuthorName = itemView.findViewById(R.id.txtAuthorName);
        }
    }
}
