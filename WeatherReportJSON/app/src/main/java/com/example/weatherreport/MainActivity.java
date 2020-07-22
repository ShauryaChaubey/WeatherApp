package com.example.weatherreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultText;

    public void checkWeather(View view) {
        try{
            DownloadTask task = new DownloadTask();
            String placeName = URLEncoder.encode(editText.getText().toString(),"UTF-8");
            Log.i("place",placeName);
            task.execute("https://openweathermap.org/data/2.5/weather?q="+ placeName + "&appid=439d4b804bc8187953eb36d2a8c26a02");
            InputMethodManager methodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"No matches found",Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1)
                {
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"No matches found",Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather Content",weatherInfo);
                JSONArray array = new JSONArray(weatherInfo);
                String message = "";
                for (int i=0; i<array.length(); i++)
                {
                    JSONObject jsonObject1 = array.getJSONObject(i);

                    String main = jsonObject1.getString("main");
                    String description = jsonObject1.getString("description");
                    if(!main.equals("") && !description.equals("")){
                        message += main + ": " + description ;
                    }
                }
                if(!message.equals("")){
                    resultText.setText(message);
                    Log.i("Weather",message);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"No matches found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        resultText = findViewById(R.id.resultTextView);
        Log.i("Check", "Started");
    }
}
