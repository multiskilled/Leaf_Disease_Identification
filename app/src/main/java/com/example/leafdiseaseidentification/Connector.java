package com.example.leafdiseaseidentification;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connector {


        public static Object connection(String urlAddress){

            try{

                URL url=new URL(urlAddress);
                HttpURLConnection httpConnection=(HttpURLConnection) url.openConnection();


                //setting properties
                httpConnection.setRequestMethod("GET");
                httpConnection.setConnectTimeout(15000);
                httpConnection.setReadTimeout(15000);
                httpConnection.setDoInput(true);


                return httpConnection;
            }
            catch (MalformedURLException e){

                e.printStackTrace();
                return ErrorTracker.WRONG_FORMAT_URL;
            }
            catch (IOException e){

                e.printStackTrace();
                return ErrorTracker.CONNECTION_ERR;
            }

        }
}
