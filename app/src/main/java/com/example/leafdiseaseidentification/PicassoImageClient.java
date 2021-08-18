package com.example.leafdiseaseidentification;

import android.content.Context;
import android.widget.ImageView;

import com.example.leafdiseaseidentification.R;
import com.squareup.picasso.Picasso;

public class PicassoImageClient {


    public static void ImageDownload(Context context, String imageLink, ImageView imageView){

        if(imageLink !=null && imageLink.length()>0){
            Picasso.get().load(imageLink).placeholder(R.drawable.image_1).into(imageView); //loading the placeolder iimage into the image view

        }else{

            Picasso.get().load(imageLink).placeholder(R.drawable.image_1).into(imageView); //loading the placeolder iimage into the image view
        }


    }


}
