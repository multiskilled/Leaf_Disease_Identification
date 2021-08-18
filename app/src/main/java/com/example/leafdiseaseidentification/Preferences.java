package com.example.leafdiseaseidentification;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

//DECLARING THE VALUES
    private Context context;
    private SharedPreferences sharedPreferences;

    //CONSTRUCTOR

    public Preferences(Context context){
        this.context=context;
        getSharedPreferences();
    }

//method to get the shared preference key
    private void getSharedPreferences(){
        sharedPreferences=context.getSharedPreferences(context.getString(R.string.my_preference_key),Context.MODE_PRIVATE);
    }


    //method to writing shared preference
    public void writePreference(){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        //write to sp
        editor.putString(context.getString(R.string.my_preference_key),"INIT_OK");
        editor.commit();

    }
//method to check the preference
    public boolean preferenceCheck(){

        boolean status=false;

        if(sharedPreferences.getString(context.getString(R.string.my_preference_key),"null").equals("null")){

            //if user opens of rthe first time-- change caue to true
            status=false;
        }else{

            status=true;
        }
        return status;
    }

    //clear the preferences
    public void preferenceClear(){
        sharedPreferences.edit().clear().commit();

    }
}
