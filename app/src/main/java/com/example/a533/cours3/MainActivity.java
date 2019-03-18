package com.example.a533.cours3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ImageView homeImage;
    String bingUrl ="https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeImage = findViewById(R.id.imageViewHomeImage);
        setListener();
    }

    private void setListener()
    {
        findViewById(R.id.buttonDownload).setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v) {
                 DownloadBingImageOfTheDayLink();
            }
         });
        findViewById(R.id.buttonConnexion).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
        moveToConnectActivity();
            }
        });
    }

    private void moveToConnectActivity(){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);

    }

    private void DownloadBingImageOfTheDayLink(){
        BingImageLinkDownloader bingImageOfTheDayLink = new BingImageLinkDownloader();
        try{

            bingImageOfTheDayLink.execute(bingUrl);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void downloadHomeImage(String imageURL){

        ImageDownloader imageDownloader = new ImageDownloader();
        try{

            imageDownloader.execute("https://www.bing.com" + imageURL);

        }catch(Exception e){
e.printStackTrace();
        }
    }


    public class ImageDownloader extends AsyncTask<String, Void , Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
               // Toast.makeText(getApplicationContext(),urlImage, Toast.LENGTH_LONG).show();

                URL url = new URL(urls[0]);
                HttpURLConnection connexion = (HttpURLConnection)url.openConnection();
                connexion.connect();
                InputStream inputStream = connexion.getInputStream();
                Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                return imageBitmap;
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
           return null;
        }
    }

    protected void onPostExecute(Bitmap bitmap){
        homeImage.setImageBitmap(bitmap);
    }

    public class BingImageLinkDownloader extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
           BufferedReader reader = null;
           HttpURLConnection connection = null;
           try{
               URL url = new URL(urls[0]);
               HttpURLConnection connexion = (HttpURLConnection)url.openConnection();
               connexion.connect();
               InputStream inputStream = connexion.getInputStream();
               StringBuffer buffer = new StringBuffer();
               if(inputStream ==null){
                   return null;
               }

               reader = new BufferedReader(new InputStreamReader(inputStream));
               String line;
               while((line=reader.readLine())!=null){
                   buffer.append(line+ "\n");
               }
               if(buffer.length() == 0){
                   return null;
               }
               return buffer.toString();
           }
           catch(MalformedURLException e){
               e.printStackTrace();
               return null;
           }catch(IOException e){
               e.printStackTrace();
               return null;
           }finally {
               if(connection !=null){
                   connection.disconnect();
               }
               if(reader != null){
                   try{
                       reader.close();
                   }catch(IOException e){
                       e.printStackTrace();
                   }
               }
           }


        }

        @Override
        protected void onPostExecute(String response) {

            if(response != null){
                JSONObject jsonRoot = null;
                try{
                    jsonRoot = new JSONObject(response);
                    JSONArray jsonArrayImage = jsonRoot.getJSONArray("images");
                    String urlImage = jsonArrayImage.getJSONObject(0).getString("url");
                    downloadHomeImage(urlImage);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }


}
