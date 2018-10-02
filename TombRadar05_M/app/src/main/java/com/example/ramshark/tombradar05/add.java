package com.example.ramshark.tombradar05;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;



public class add extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText bdate;
    EditText ddate;
    EditText area;
    EditText blk;
    EditText lot;
    String Fname, Lname, Bdate, Ddate,Area,Blk,Lot;

    String lat,lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getSupportActionBar().setTitle("Add Grave Details");

        fname= (EditText) findViewById(R.id.fname);
        lname= (EditText) findViewById(R.id.lname);
        bdate= (EditText) findViewById(R.id.bdate);
        ddate= (EditText) findViewById(R.id.ddate);
        area= (EditText) findViewById(R.id.area);
        blk= (EditText) findViewById(R.id.blk);
        lot= (EditText) findViewById(R.id.lot);

        Intent intent= getIntent();

        lat=intent.getStringExtra("lat");
        lang=intent.getStringExtra("lang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }

    public void addgrave (View view) {

        Fname=fname.getText().toString();
        Lname=lname.getText().toString();
        Bdate=bdate.getText().toString();
        Ddate=ddate.getText().toString();
        Area=area.getText().toString();
        Blk=blk.getText().toString();
        Lot=lot.getText().toString();
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(Fname,Lname,Bdate,Ddate,lat,lang,Area,Blk,Lot);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    class BackgroundTask extends AsyncTask<String,Void, String> {

        String add_URL;

        @Override
        protected void onPreExecute() {
            add_URL="http://tombradar.000webhostapp.com/add.php";
        }


        @Override
        protected String doInBackground(String... args) {
            String Fname,Lname,Bdate,Ddate,Lat,Lang,Area,Blk,Lot;

            Fname= args[0];
            Lname= args[1];
            Bdate= args[2];
            Ddate= args[3];
            Lat  = args[4];
            Lang = args[5];
            Area = args[6];
            Blk = args[7];
            Lot = args[8];

            try {
                URL url = new URL(add_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data_string= URLEncoder.encode("Fname","UTF-8")+"="+URLEncoder.encode(Fname,"UTF-8")+"&"+
                        URLEncoder.encode("Lname","UTF-8")+"="+URLEncoder.encode(Lname,"UTF-8") +"&"+
                        URLEncoder.encode("Bdate","UTF-8")+"="+URLEncoder.encode(Bdate,"UTF-8") +"&"+
                        URLEncoder.encode("Ddate","UTF-8")+"="+URLEncoder.encode(Ddate,"UTF-8") +"&"+
                        URLEncoder.encode("Lat","UTF-8")+"="+URLEncoder.encode(Lat,"UTF-8") +"&"+
                        URLEncoder.encode("Lang","UTF-8")+"="+URLEncoder.encode(Lang,"UTF-8")+"&"+
                        URLEncoder.encode("Area","UTF-8")+"="+URLEncoder.encode(Area,"UTF-8")+"&"+
                        URLEncoder.encode("Blk","UTF-8")+"="+URLEncoder.encode(Blk,"UTF-8")+"&"+
                        URLEncoder.encode("Lot","UTF-8")+"="+URLEncoder.encode(Lot,"UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();

                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Grave Added";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String result) {


            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
