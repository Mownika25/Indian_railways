package com.example.android.railapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGrid = findViewById(R.id.mainGrid);
        
        //SET EVENT
        setSingleEvent(mainGrid);
    }


    private void setSingleEvent(GridLayout mainGrid)
    {
        for (int i=0;i<mainGrid.getChildCount();i++)
        {
            CardView cardView =(CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (finalI==0){Intent intent = new Intent(MainActivity.this,spotTrains.class);startActivity(intent);}
                    else if (finalI==1){Intent intent = new Intent(MainActivity.this,findTrains.class);startActivity(intent);}
                    else if (finalI==2){Intent intent = new Intent(MainActivity.this,availableSeats.class);startActivity(intent);}


                    else if (finalI==3){Intent intent = new Intent(MainActivity.this,trainRoute.class);startActivity(intent);}


                }
            });
        }
    }
}