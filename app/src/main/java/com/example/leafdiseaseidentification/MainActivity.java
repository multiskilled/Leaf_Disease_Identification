package com.example.leafdiseaseidentification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
/*

//firebase imports
//under development
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
 */
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/*
CREATED BY MUKESH
*/

public class MainActivity extends  AppCompatActivity {

    //DECLARING  NOTIFICATION MANAGER AND ID
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    public ImageView imageView;
    private static final int CAMERA_PERMISSION_REQUEST_CODE=1000;
    private static final int CAMERA_REQUEST_CODE=10001;
    int choose_photo=1;
    public TextView resultTextView;


    //setUp for tensorflow
    public static final int SIZE_OF_INPUT = 224;    //INPUT SIZE VALUE
    public static final int IMG_MEAN = 128;   //IMAGE MEAN VALUE
    public static final float IMG_STD = 128.0f; //IMAGE STANDARD DEVIATION
    public static final String INPUT = "input";
    public static final String FINAL_RESULT = "final_result";
    public static final String MODEL = "retrained_graph.pb";
    public static final String LABEL = "retrained_labels.txt";

    public Classifier classifier;
    public ImageView iV1;
    public Bitmap mDiagnosisBitmap;
public String s;
public Bitmap bm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);


        Button from_gallery=findViewById(R.id.from_gallery);
        imageView=findViewById(R.id.image_capture);


        resultTextView=findViewById(R.id.resultTextView);


        Button Bt_image_capture=findViewById(R.id.take_picture);

        from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fr_gall= new Intent(Intent.ACTION_PICK);
                fr_gall.setType("image/*");
                startActivityForResult(fr_gall,choose_photo);
            }
        });



        Bt_image_capture.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                if(hasPermission()){
                    openCamera();
                }
                else{
                    requestPermission();
                }
            }
        });
/*
// Firebase Cloud Vision AI
//Under Development

        FirebaseAutoMLRemoteModel remoteModel; // For loading the model remotely
        FirebaseVisionImageLabeler labeler; //For running the image labeler
        FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder optionsBuilder; // Which option is use to run the labeler local or remotely
        ProgressDialog progressDialog; //Show the progress dialog while model is downloading...
        FirebaseModelDownloadConditions conditions; //Conditions to download the model
        FirebaseVisionImage image; // preparing the input image
        private FirebaseAutoMLLocalModel localModel;
*/
        final AssetManager assetManager = getAssets();
        classifier = ImageClassifierTf.create(assetManager,MODEL,LABEL,SIZE_OF_INPUT,IMG_MEAN,IMG_STD, INPUT, FINAL_RESULT);


        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        imageView = (ImageView) findViewById(R.id.image_capture);
        Button detect = (Button) findViewById(R.id.detect);

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<Classifier.Recognition> results = classifier.recognizeImage(mDiagnosisBitmap);
                System.out.println(results.size());
                for (final Classifier.Recognition result: results){
                    System.out.println("Result:"+result.getTitle()+" "+result.getConfidence()+result.toString());

                }
                resultTextView.setText(results.get(0).getTitle()+"  Confidence:"+results.get(0).getConfidence());
                sendNotification();
            }
        });
        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Content.class);
                //starting second activity
                s=resultTextView.getText().toString();
                intent.putExtra("resultDetect", s);
                bm=mDiagnosisBitmap;
                intent.putExtra("mDiagnosisBitmap",bm);
                startActivity(intent);

            }
        });


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        //for Feed Selection
        bottomNavigationView.setSelectedItemId(R.id.detect);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);
        //action for item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.feed:
                        startActivity(new Intent(getApplicationContext()
                                ,Feed.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.detect:
                        return true;
                    case R.id.help:
                        startActivity(new Intent(getApplicationContext()
                                ,HelpQueries.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                ,SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }

    public NotificationCompat.Builder sendNotification() {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this).
                setContentTitle("Leaf Detection")
                .setContentText("Disease is detected.")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24);
        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);

        return notifyBuilder;
    }




    //display the captured image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //FOR CAMERA CAPTURE DETECTION
      /*  if(requestCode==CAMERA_REQUEST_CODE){

            Bitmap selectedImage = null;
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
              selectedImage = BitmapFactory.decodeStream(imageStream);
                //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Success!!!");
            mDiagnosisBitmap = scaleImage(selectedImage);
            imageView.setImageBitmap(mDiagnosisBitmap);

        }
        */

        if(requestCode == choose_photo && resultCode == RESULT_OK && data != null && data.getData() != null){
            Bitmap selectedImage = null;
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
              selectedImage = BitmapFactory.decodeStream(imageStream);
                //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Success!!!");
            mDiagnosisBitmap = scaleImage(selectedImage);
            imageView.setImageBitmap(mDiagnosisBitmap);



        }

        super.onActivityResult(requestCode, resultCode, data);
    }

//firebase image loader
/*
    private void processImageLabeler(FirebaseVisionImageLabeler labeler, FirebaseVisionImage image) {
        labeler.processImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionImageLabel>> task) {
                progressDialog.cancel();
                for (FirebaseVisionImageLabel label : task.getResult()) {
                    String eachlabel = label.getText().toUpperCase();
                    float confidence = label.getConfidence();
                    results.append(eachlabel + " - " + ("" + confidence * 100).subSequence(0, 4) + "%" + "\n\n");
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.google.com/search?q=" + task.getResult().get(0).getText()));
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("OnFail", "" + e);
                Toast.makeText(MainActivity.this, "Something went wrong! " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    */

    //handling camera permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE){

            if(hasAllPermissions(grantResults)){
                openCamera();
            }else{
                requestPermission();
            }
        }

    }

    private boolean hasAllPermissions(int[] grantResults) {
        for(int result : grantResults){
            if(result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

//request permission
    private void requestPermission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(this,"Camera Permisssion  Required",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

    }
    //open camera intent to take pricture
    private void openCamera() {
        Intent IntentForCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(IntentForCamera,CAMERA_REQUEST_CODE);
    }

    private boolean hasPermission() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            return checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

//method for loading the slides
    //welocme activity
    public void LoadImgSlides(View view) {

         new Preferences(this).preferenceClear();
         startActivity(new Intent(this,WelcomeActivity.class));
         finish();
    }
//method for scaling the image based on the input size
    //creating a new scale image
    //returning the scaled image
    public Bitmap scaleImage(Bitmap bitmap){
        int orignalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = ((float)SIZE_OF_INPUT)/orignalWidth;
        float scaleHeight = ((float)SIZE_OF_INPUT)/originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap,0,0,orignalWidth,originalHeight,matrix,true);
        return scaledBitmap;
    }


/*
    private void initTensorFlowAndLoadModel() {
                try {
                    classifier = (Classifier) TfImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,QUANT);

                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }


*/
}