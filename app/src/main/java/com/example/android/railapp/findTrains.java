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

public class findTrains extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    String dest,date;
    String src;
    EditText editText1;
    EditText editText2,editText3;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_trains);

        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonparse = findViewById(R.id.button_parse);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        pd = new ProgressDialog(findTrains.this);
        pd.setMessage("Loading . . .");// Loading action


        mQueue = Volley.newRequestQueue(this);

        buttonparse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // PERFORM VALIDATION IF  THE USER ENTERS INCORRECT DATA
                if(editText1.length()<2 ){
                    Toast.makeText(getApplicationContext(), " Source incorrect. ", Toast.LENGTH_SHORT).show();
                }
                else if(editText2.length()<2 ){
                    Toast.makeText(getApplicationContext(), " Destination incorrect. ", Toast.LENGTH_SHORT).show();
                }
              else  if(editText3.length()<10 ){
                    Toast.makeText(getApplicationContext(), " Date incorrect. ", Toast.LENGTH_SHORT).show();
                }
                else {
                    mTextViewResult.setText(null);
                    src = editText1.getText().toString();
                    dest = editText2.getText().toString();
                    date = editText3.getText().toString();
                    jsonParse();
                }
            }
        });
    }

    private void jsonParse() {
        pd.show();
        Log.d("FIRE", "Service Called");
        String url = "https://api.railwayapi.com/v2/between/source/" + src + "/dest/" + dest + "/date/"+date+"/apikey/0vuuwbb1nr/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            String name ="",tr_time = "", dep_time = "",ar_time = "", no = "";
                            Log.d("FIRE", "response success");
                            //gets all the available trains between stations which is contained in json array named trains.
                            JSONArray jsonArray = response.getJSONArray("trains");

                            //iterate through json array and extract each field related to train
                            for (int i = 0 ;i < jsonArray.length() ; i++){
                                JSONObject t = jsonArray.getJSONObject(i);

                                name = t.getString("name");
                                no = t.getString("number");
                                tr_time = t.getString("travel_time");
                                dep_time = t.getString("src_departure_time");
                                ar_time = t.getString("dest_arrival_time");
                                mTextViewResult.append("Name: " + name + ", "  + no + "\n" + "Travel time: " + tr_time +"\n"+"Soure Departure Time: "+dep_time+"\n"+"Destination Arrival Time: "+ar_time+"\n\n\n" );

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