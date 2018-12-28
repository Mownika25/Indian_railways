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

import org.json.JSONException;

import android.app.ProgressDialog;

public class trainRoute extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    String no;
    EditText editText;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_route);

        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonparse = findViewById(R.id.button_parse);
        editText = findViewById(R.id.editText2);
        pd=new ProgressDialog(trainRoute.this);
        pd.setMessage("Loading . . .");

        mQueue = Volley.newRequestQueue(this);

        buttonparse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.length()<5){
                    Toast.makeText(getApplicationContext(), " Train No. Incorrect. ", Toast.LENGTH_SHORT).show();
                }
                else {
                    mTextViewResult.setText(null);
                    no = editText.getText().toString();
                    jsonParse();
                }

            }
        });
    }

    private void jsonParse() {
        pd.show();
        Log.d("FIRE", "Service Called");
        String url = "https://api.railwayapi.com/v2/route/train/"+no+"/apikey/0vuuwbb1nr/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String dep = "",arr="";
                            Log.d("FIRE", "response success");

                            /*JSONObject from_station = response.getJSONObject("from_station");
                            JSONObject to_station = response.getJSONObject("to_station");


                            String tname = to_station.getString("name");
                            mTextViewResult.append("Source:  " + fname + " \n\n" + "Destination:  " + tname +"\n\n");*/

                            JSONArray jsonArray = response.getJSONArray("route");

                            for (int i = 0 ;i < jsonArray.length() ; i++){
                                JSONObject r = jsonArray.getJSONObject(i);

                                JSONObject stn  = r.getJSONObject("station");

                                String stn_name = stn.getString("name");
                                String stn_code = stn.getString("code");

                                dep = r.getString("schdep");
                                arr = r.getString("scharr");
                                mTextViewResult.append( "Station:  "  + stn_name + ", " + stn_code + "\n" + "Scheduled Arrival: "  + arr + "\n" + "Scheduled Departure: " + dep + "\n\n\n" );
                                pd.hide();

                            }


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