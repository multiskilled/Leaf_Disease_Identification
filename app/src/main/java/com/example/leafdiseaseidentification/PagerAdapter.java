package com.example.leafdiseaseidentification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

//DECLARING THE VARIABLES
    private int[] layouts;
    private LayoutInflater layoutInflater;
    private Context context;


    //CONSTRUCTOR
    public PagerAdapter(int[] layouts, Context context) {
        this.layouts = layouts;
        this.context = context;
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view =layoutInflater.inflate(layouts[position],container,false);
        //adding vie wto co
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //create varibale for view
        //type case itno view
        //type cast to object
        View view=(View) object;
        container.removeView(view);
        // or container.removeView((View)object);
    }

}
