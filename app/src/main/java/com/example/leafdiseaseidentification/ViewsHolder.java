package com.example.leafdiseaseidentification;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewsHolder extends RecyclerView.ViewHolder{

        TextView titleTxt,desctxt,dateTxt;
        ImageView img;
//view holder returns the views
        //views for card model
public ViewsHolder(View itemView){
        super(itemView);

        titleTxt=(TextView)itemView.findViewById(R.id.feedTitle);
        desctxt=(TextView)itemView.findViewById(R.id.DescriptionText);
        dateTxt=(TextView)itemView.findViewById(R.id.datetext);
        img=(ImageView)itemView.findViewById(R.id.feedImage);
        }

        }

