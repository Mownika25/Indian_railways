package com.example.android.railapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.app.ProgressDialog;

import android.widget.Toast;



public class spotTrains extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    String train,travel_date,last_location;
    EditText editText,date;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_trains);
        pd=new ProgressDialog(spotTrains.this);
        pd.setMessage("Loading . . .");

        mTextViewResult = findViewById(R.id.textView);
        Button buttonparse = findViewById(R.id.button_parse);
        editText=findViewById(R.id.editText);
        date=findViewById(R.id.date);

        mQueue = Volley.newRequestQueue(this);

        buttonparse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.length() < 5) {
                    Toast.makeText(getApplicationContext(), " Train No. Incorrect. ", Toast.LENGTH_SHORT).show();
                }
                else if (date.length() < 10) {
                    Toast.makeText(getApplicationContext(), " Incorrect Date. ", Toast.LENGTH_SHORT).show();
                }
                    else {

                    mTextViewResult.setText(null);
                    train = editText.getText().toString();
                    travel_date = date.getText().toString();
                    jsonParse();
                }
            }
        });
    }

    private void jsonParse() {
        pd.show();
        Log.d("FIRE","Service Called");
        String url = "https://api.railwayapi.com/v2/live/train/"+train+"/date/"+travel_date+"/apikey/0vuuwbb1nr/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("FIRE","response success");// DEBUG
                            JSONArray jsonArray = response.getJSONArray("route");
                            // GET THE LATEST UPDATED LOCATION AND SHOW
                            last_location = response.getString("position");
                            mTextViewResult.append("#LATEST UPADTE:\n"+last_location);

                            mTextViewResult.append("\nBELOW IS THE COMPLETE RUN TILL LAST UPADTE\n\n");
                            // COMPLETE RUN SHOWING EACH DETAIL OF TRAIN AT EACH STOP UNTIL THE LAST STOP

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject rou = jsonArray.getJSONObject(i);
                                String position  = response.getString("position");
                                JSONObject stn  = rou.optJSONObject("station");
                                String stnname = stn.getString("name");
                                String stncode = stn.getString("code");
                                boolean has_arrrived = rou.getBoolean("has_arrived");
                                boolean has_departed = rou.getBoolean("has_departed");
                                String scharr = rou.getString("scharr");
                                String schdep = rou.getString("schdep");
                                String actarr = rou.getString("actarr");
                                String actdep = rou.getString("actdep");
                                int late = rou.getInt("latemin");

                                if(has_arrrived == false && has_departed == false)
                                    break;
                                mTextViewResult.append("\nStatus At "+stnname+"\n");
                                mTextViewResult.append("*Scheduled Arrival- "+scharr+"\n"+"*Actual Arrival- "+actarr+"\n");
                                mTextViewResult.append("*Scheduled Departure- "+schdep+"\n"+"*Actual Departure- "+actdep+"\n");
                                mTextViewResult.append("\n*Delayed By "+late+" Minutes\n\n");

                            }
                            pd.hide();
                        }
                        // RESPONSE FAILURE
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("FIRE","Error");
                            pd.hide();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Log.d("FIRE","Error"); DEBBUG
            }
        });

        mQueue.add(request);
    }
}