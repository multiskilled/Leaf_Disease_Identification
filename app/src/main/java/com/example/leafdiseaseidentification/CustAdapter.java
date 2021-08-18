package com.example.leafdiseaseidentification;
/*
CREATED BY MUKESH
*/

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;


//custom adapter for views
public class CustAdapter extends RecyclerView.Adapter<ViewsHolder>


{
    //declaring context and array list for  blog posts
    //retirved from teh website
    Context c;
    ArrayList<BlogPost> articles;



    //constructor for custom adapter
    public CustAdapter(Context c, ArrayList<BlogPost> articles) {
        this.c = c;
        this.articles = articles;
    }

    @Override
    public ViewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.cardmodel,parent,false);
        return new ViewsHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewsHolder holder, int position) {

        BlogPost article=articles.get(position);
       String imageUrl=article.getImageUrl();

        holder.titleTxt.setText(article.getBlog_title());
        holder.desctxt.setText(article.getBlog_desc().substring(0,130));
        holder.dateTxt.setText(article.getDate());
        PicassoImageClient.ImageDownload(c,imageUrl,holder.img); //for holding the retrived image



    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}

