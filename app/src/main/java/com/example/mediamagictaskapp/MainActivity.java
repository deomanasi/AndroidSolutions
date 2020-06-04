package com.example.mediamagictaskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.mediamagictaskapp.Adapter.RecyclerViewAdapter;
import com.example.mediamagictaskapp.Model.Author;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public String TAG = "MainActivity";
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    String parseUrl = "https://picsum.photos/list";
    URL url;
    String author_name = "", id = "";

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetDataFromJson().execute();
    }

    //get author details
    public class GetDataFromJson extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            Log.i("Example", "onPreExecute Called");

            //this method will be running on UI thread
            pDialog.setMessage("\tLoading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://picsum.photos/list");
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            try
            {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(false);

            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return e1.toString();
            }

            try
            {
                int response_code = conn.getResponseCode();
                Log.d(TAG,"response_code = " + response_code);
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());

                }
                else
                    {
                    return ("unsuccessful");
                }

            }
            catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            Log.d(TAG,"s = " + s);

            Log.i("Example", "onPostExecute Called");
//            pDialog.dismiss();

            List<Author> authorList = new ArrayList<>();
            //pDialog.dismiss();

            try
            {
                JSONArray jArray = new JSONArray(s);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Author author = new Author();

                    author.id = json_data.getInt("id");
                    author.author_name = json_data.getString("author");

                    Log.d(TAG,"print values = " + author.id + " - " + author.author_name);

                    authorList.add(author);
                }

                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
                adapter = new RecyclerViewAdapter(MainActivity.this, authorList);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                recyclerView.setAdapter(adapter);

                pDialog.dismiss();
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
                ex.toString();
            }
        }
    }
}
