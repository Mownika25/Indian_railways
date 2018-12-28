package com.example.android.railapp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;

public class availableSeats extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    String dest,date,pr;
    String src;
    String jc;
    String train;
    EditText editText;
    EditText editText2;
    EditText editText3;
    EditText editText4,editText5,editText6;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_seats);
        pd=new ProgressDialog(availableSeats.this);
        pd.setMessage("Loading . . .");
        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonparse = findViewById(R.id.button_parse);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);

        mQueue = Volley.newRequestQueue(this);

        buttonparse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText5.length() < 10) {
                    Toast.makeText(getApplicationContext(), " Incorrect Date. ", Toast.LENGTH_SHORT).show();
                }
                else if(editText.length()<5){
                    Toast.makeText(getApplicationContext(), " Train No. Incorrect. ", Toast.LENGTH_SHORT).show();
                }

                else {
                    mTextViewResult.setText(null);
                    src = editText3.getText().toString();
                    dest = editText4.getText().toString();
                    jc = editText2.getText().toString();
                    train = editText.getText().toString();
                    date = editText5.getText().toString();
                    pr = editText6.getText().toString();
                    jsonParse();

                }
            }
        });
    }

    private void jsonParse() {
        pd.show();
        Log.d("FIRE", "Service Called");
        String url = "https://api.railwayapi.com/v2/check-seat/train/" + train + "/source/" + src + "/dest/" + dest + "/date/"+date+"/pref/"+pr+"/quota/" + jc + "/apikey/0vuuwbb1nr/";
/*Calls railway api and extracts the json response and displays the specified information*/
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String d ="",s="";
                            Log.d("FIRE", "response success");
                            JSONObject from_station = response.getJSONObject("from_station");
                            JSONObject to_station = response.getJSONObject("to_station");

                            String fname = from_station.getString("name");
                            String tname = to_station.getString("name");
                            mTextViewResult.append("Source:  " + fname + " \n\n" + "Destination:  " + tname +"\n\n");

                            JSONArray jsonArray = response.getJSONArray("availability");//gets the json array "availability" and fetches individual response

                            for (int i = 0 ;i < jsonArray.length() ; i++){
                                JSONObject avail = jsonArray.getJSONObject(i);

                                /*extracts the date and available out of the obtained response"*/
                                d = avail.getString("date");
                                s = avail.getString("status");
                                mTextViewResult.append("Date: " + d + "\n\n" + "Status " + s + "\n\n");
                            }



                            pd.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("FIRE", "Error");
                        pd.hide();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("FIRE", "Error");
            }
        });

        mQueue.add(request);
    }
}