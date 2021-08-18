package com.example.leafdiseaseidentification;
/*
CREATED BY MUKESH
*/

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

//Class data downloader extends form async task, because we are deriving form it and for downloading thh data
public class DataDownloader extends AsyncTask<Void,Void, Object> {

//declaring the variables
    Context context;
    String urlAddress;
    ListView LstView;
    RecyclerView rv;
    ProgressDialog progressDialog;


    //constructor
    public DataDownloader(Context context, String urlAddress, RecyclerView rv) {
        this.context=context;
        this.rv=rv;
        this.urlAddress=urlAddress;
    }

    //method before fetching the blog posts
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Fetch Blog Posts");
        progressDialog.setMessage("Loading.. Please Wait");
        progressDialog.show();

    }



    @Override
    protected Object doInBackground(Void... params) {
        return dataDownload();
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        progressDialog.dismiss();

        //dissmiss teh dialog after loading

        if(data.toString().startsWith("ERR")){

            Toast.makeText(context, data.toString(),Toast.LENGTH_SHORT).show();
        }
else {

    //parse the data, input stream and then execute
            new ParserForRSS(context, (InputStream) data, rv).execute();
        }
    }

    private Object dataDownload(){

        Object connectionToUrl=Connector.connection(urlAddress);

        if(connectionToUrl.toString().startsWith("ERR")){
            return connectionToUrl.toString();

        }

        try{
            HttpURLConnection httpConn=(HttpURLConnection) connectionToUrl;
            int responseCode=httpConn.getResponseCode();

            if(responseCode==httpConn.HTTP_OK){
                InputStream InpStream=new BufferedInputStream(httpConn.getInputStream());
                return InpStream;

            }
            return ErrorTracker.RESPONSE_ERR + httpConn.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();

            return ErrorTracker.IO_ERROR;
        }

    }
}
