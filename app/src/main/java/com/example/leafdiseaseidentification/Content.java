package com.example.leafdiseaseidentification;

/*
CREATED BY MUKESH
*/

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Content extends AppCompatActivity {


    FloatingActionButton floatingActionButton;
    TextView detectedName;
    ImageView detectedImage;
    Bitmap b;
    String detectNameString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentdisplay);


        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent().setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.ACTION_ATTACH_DATA,b);
                i.putExtra(Intent.EXTRA_TEXT,"we are Sharing the data");
                i.setType("text/plain");
                startActivity(i);
            }
        });


        detectedName=findViewById(R.id.detectedName);
        //detectedName.setText(mainActivity.resultTextView.getText());

        detectedImage=findViewById(R.id.imageView);
        Intent intent=getIntent();
        detectNameString=getIntent().getExtras().getString("resultDetect");
        detectedName.setText(detectNameString);
        b=intent.getParcelableExtra("mDiagnosisBitmap");
        detectedImage.setImageBitmap(b);
}

}
