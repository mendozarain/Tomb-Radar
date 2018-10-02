package com.example.ramshark.tombradarg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    String JsonString;
    String datastring;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button Badd=(Button) findViewById(R.id.vadd);
        Button Bsearch=(Button) findViewById(R.id.vsearch);
        TextView status = (TextView) findViewById(R.id.status);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        getSupportActionBar().setTitle("Tomb Radar G");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        if(networkInfo != null){

            status.setVisibility(View.INVISIBLE);


        }

        else{


            Badd.setEnabled(false);
            Bsearch.setEnabled(false);
        }


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }



    }


    public void viewsearch (View view) {





        startActivity(new Intent(this,search.class));





    }








    public void viewaddmap (View view) {

   startActivity(new Intent(this, com.example.ramshark.tombradarg.add_map.class));

    }






}


