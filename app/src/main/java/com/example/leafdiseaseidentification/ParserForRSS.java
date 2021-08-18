package com.example.leafdiseaseidentification;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ParserForRSS extends AsyncTask<Void,Void, Boolean> {

    Context context;
    InputStream InStream; //receibign from downlaoder class
    //ListView listView;
    RecyclerView rv;
    //aray list for stroing
    ArrayList<BlogPost> blogPosts=new ArrayList<>();

    ProgressDialog progressDialog;

    public ParserForRSS(Context context, InputStream inStream, RecyclerView rv) {
        this.context = context;
        InStream = inStream;
        this.rv = rv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Parsing Blog Posts");
        progressDialog.setMessage("Parsing.. Please Wait");
        progressDialog.show();

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseRSSData();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);
        progressDialog.dismiss();

        if(isParsed){
            rv.setAdapter(new CustAdapter(context, blogPosts));
            //Bind
        }else{

            Toast.makeText(context,"UNABLE TO PARSE DATA", Toast.LENGTH_SHORT).show();
        }


}
    private Boolean parseRSSData(){

        try{

            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();

            parser.setInput(InStream,null);

            int event=parser.getEventType();

            String tagValue=null;
            Boolean isSiteMeta=true;

            blogPosts.clear();
            BlogPost blogPost=new BlogPost();

            //after checking with each tags in teh rss then work with the values
            do{

                String tagName=parser.getName();//name of tag

                //swithc through events
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if(tagName.equals("item"))
                        {
                            blogPost=new BlogPost();
                            isSiteMeta=false; //we dont need site description so ithis ill be fales
                        }
                        break;

                    case XmlPullParser.TEXT: //text value
                        tagValue=parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        //if its is not site meta and if its an article / or blog post thne--
                        if(!isSiteMeta){

                            if(tagName.equals("title")){

                                blogPost.setBlog_title(tagValue);
                            }else if(tagName.equals("description")){

                                String descrip=tagValue;
                                blogPost.setBlog_desc(descrip);

                                //extract the image from the description
                                //5 adn 3 are to take the index from the 5 th element after src= image url to jpg(3 cut off)
                                String pictureUrl=descrip.substring(descrip.indexOf("src=")+5,descrip.indexOf("jpg")+3);
                                blogPost.setImageUrl(pictureUrl);

                            }
                            else if(tagName.equals("pubDate")){
                                blogPost.setDate(tagValue);
                            }
                        }
                        if(tagName.equals("item")){

                            blogPosts.add(blogPost);
                            isSiteMeta=true;
                        }
                        break;
                }

                event=parser.next();
                //if we haven to reached end of focment
            }while (event != XmlPullParser.END_DOCUMENT);

            //if successfull
            return true;

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        //if not successfull
        return false;
    }

}

