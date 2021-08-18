package com.example.leafdiseaseidentification;


/*
CREATED BY MUKESH
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

//class for welcome screen and implements on click listener for the buttons
public class WelcomeActivity extends  AppCompatActivity implements View.OnClickListener{



//Declaring the variables
    ViewPager viewPager; //delcare teh view pager
    //declaring the list of slides
    public int[] layouts={R.layout.firstslide,R.layout.secondslide, R.layout.thirdslide, R.layout.fourthslide };
    PagerAdapter pagerAdapter; //from page adapter class
    private Button skipButton1, NextButton; //buttons
    private LinearLayout Dots;        //linear layout for dots above the skip and next button
    private ImageView[] DotsImg;


    Button skipButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomeactivity);
        if(new Preferences(this).preferenceCheck()){
            StartHome();
        }

        //transparent bar
        if(Build.VERSION.SDK_INT>19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //view pager
        viewPager= findViewById(R.id.viewpager);


        pagerAdapter=new PagerAdapter(layouts,this);
        viewPager.setAdapter(pagerAdapter);


        Dots=(LinearLayout)findViewById(R.id.dots);
        //dots with position of 0
        buildDots(0);


        skipButton1=(Button)findViewById(R.id.skipButton1);
        NextButton=(Button)findViewById(R.id.NextButton);
        //reister the listeners for button
        skipButton1.setOnClickListener(this);
        NextButton.setOnClickListener(this);
        //event liseners for view pager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //slideing dots with teh image
            buildDots(position);
            if(position==layouts.length-1){
                //change button to start from skikp
                NextButton.setText("Start");
                skipButton1.setVisibility(View.INVISIBLE);
            }else{

                NextButton.setText("Next");
                skipButton1.setVisibility(View.VISIBLE);
            }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        skipButton= findViewById(R.id.skipButton1);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
                //starting second activity
                startActivity(intent);
            }
        });


    }

    private void buildDots(int present_position){

        if(Dots!=null)
            Dots.removeAllViews();
        //initialise the image view array
        DotsImg=new ImageView[layouts.length];


        for(int i=0;i<layouts.length;i++){

            DotsImg[i]=new ImageView(this);
            if(i==present_position)
            {
                DotsImg[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dots_active));
            }
            else{
                DotsImg[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.dots_inactive));
            }
            //creater layot  pramertes for leinaer layotuo
            //specify width and hieght f layout
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // specify margins
            params.setMargins(4,0,4,0);
            //layout pareamets
            // image view ot linear layout
                   Dots.addView(DotsImg[i],params);


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.NextButton:
                startNextSlide();
                break;

            case R.id.skipButton1:
                StartHome();
                //when clicked skip - start home adn write the shaed prefernce / write
                new Preferences(this).writePreference();
                break;
        }
    }

    private void StartHome(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    //handel the next slide button click

    private void startNextSlide(){
        int nxt_slide=viewPager.getCurrentItem()+1;

        if(nxt_slide<layouts.length){
            viewPager.setCurrentItem(nxt_slide);

        }else{

            StartHome();
            new Preferences(this).writePreference();
        }

    }
}