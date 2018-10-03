package app.sample.app.locate;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView details;
    private Toolbar toolbar;

    private String locality , postalcode ,countryname ,city ,address ,countrycode;
    private double latitude , longitude ;

    database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        details = findViewById(R.id.locationdetails);
        toolbar = findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);
//
//        locality = getIntent().getStringExtra("locality");
//        postalcode = getIntent().getStringExtra("postalcode");
//        countrycode = getIntent().getStringExtra("countrycode");
//        city = getIntent().getStringExtra("city");
//        address = getIntent().getStringExtra("Address");
//        countryname = getIntent().getStringExtra("countryname");
//
//        latitude = getIntent().getDoubleExtra("latitude" , 0);
//        longitude = getIntent().getDoubleExtra("longitude" , 0);
        db = new database(this);

        Button b  = toolbar.findViewById(R.id.mapss);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        Button b1 = findViewById(R.id.userlocations);
        Button b2 = findViewById(R.id.searchedlocations);

        b1.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Cursor res = db.getData();
         String[] data = {""};
         List<String> dataset = new ArrayList<>();

         while(res.moveToNext())
         {
             data = new String[]{res.getString(0), "Address :: \n" + res.getString(1), "Locality :: \n" + res.getString(2),
                     "City :: \n" + res.getString(3), "Country Name :: \n" + res.getString(4), "Country Code :: \n" + res.getString(5),
                     "Postal Code :: " + res.getString(6), "Latitude :: " + res.getString(7) + "\n" +
                     "Longitude :: " + res.getString(8)};
             Collections.addAll(dataset, data);

         }
         final ArrayAdapter<String> adapter2 =  new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1   ,dataset);
         details.setAdapter(adapter2);
     }
 });
 b2.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Cursor res = db.getDatafromtable2();
         String[] data = {""};
         List<String> dataset = new ArrayList<>();

         while(res.moveToNext())
         {
             data = new String[]{res.getString(0), "Address :: \n" + res.getString(1), "Locality :: \n" + res.getString(2),
                     "City :: \n" + res.getString(3), "Country Name :: \n" + res.getString(4), "Country Code :: \n" + res.getString(5),
                     "Postal Code :: " + res.getString(6), "Latitude :: " + res.getString(7) + "\n" +
                                "Longitude :: " + res.getString(8)};

             Collections.addAll(dataset, data);

         }
         final ArrayAdapter<String> adapter =  new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1   ,dataset);
         details.setAdapter(adapter);
     }
 });



    }
}
