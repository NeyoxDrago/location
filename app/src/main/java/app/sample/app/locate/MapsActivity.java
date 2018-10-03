package app.sample.app.locate;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.BinderThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Toolbar toolbar;
    private String locality, postalcode, countryname, city, address, countrycode;
    private double latitude, longitude;
    private ProgressDialog progress;

    database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new database(this);

        progress = new ProgressDialog(this);
        progress.setMessage("Getting Location");
        progress.setCancelable(false);
        progress.show();

        Button b = toolbar.findViewById(R.id.back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(a);
            }
        });

        final Button locate = findViewById(R.id.mylocation);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        Geocoder geo = new Geocoder(getApplicationContext());
                        try {
                            List<Address> addressList= geo.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            locality = addressList.get(0).getFeatureName();
                            postalcode = addressList.get(0).getPostalCode();
                            latitude = addressList.get(0).getLatitude();
                            longitude = addressList.get(0).getLongitude();
                            countryname = addressList.get(0).getCountryName();
                            countrycode = addressList.get(0).getCountryCode();
                            city = addressList.get(0).getAdminArea();
                            address = addressList.get(0).getAddressLine(0);

                            LatLng lat = new LatLng(latitude,longitude);
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(lat).title("selected position"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,15.0f));
                            progress.dismiss();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        });


        final Button done = toolbar.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               boolean result = db.insert(address ,locality , city,countryname ,countrycode ,postalcode,latitude+"",longitude+"");
               if(result)
               {
                   Toast.makeText(MapsActivity.this, "Location Successfully updated", Toast.LENGTH_SHORT).show();
               }
               else
                   Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

          ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION ,
                  Manifest.permission.ACCESS_FINE_LOCATION},2);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    double x = location.getLatitude();
                    double y = location.getLongitude();

                    Geocoder geo = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList= geo.getFromLocation(x,y,1);
                        locality = addressList.get(0).getFeatureName();
                        postalcode = addressList.get(0).getPostalCode();
                        latitude = addressList.get(0).getLatitude();
                        longitude = addressList.get(0).getLongitude();
                        countryname = addressList.get(0).getCountryName();
                        countrycode = addressList.get(0).getCountryCode();
                        city = addressList.get(0).getAdminArea();
                        address = addressList.get(0).getAddressLine(0);

                        LatLng lat = new LatLng(latitude,longitude);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(lat).title("selected position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,15.0f));
                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }



    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                Geocoder geo = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList= geo.getFromLocation(latLng.latitude,latLng.longitude,2);
                    locality = addressList.get(0).getFeatureName();
                    postalcode = addressList.get(0).getPostalCode();
                    latitude = addressList.get(0).getLatitude();
                    longitude = addressList.get(0).getLongitude();
                    countryname = addressList.get(0).getCountryName();
                    countrycode = addressList.get(0).getCountryCode();
                    city = addressList.get(0).getAdminArea();
                    address = addressList.get(0).getAddressLine(0);

                    LatLng lat = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(lat).title("selected position"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,15.0f));
                    progress.dismiss();
                    boolean result = db.inserttotable2(address ,locality , city,countryname ,countrycode ,postalcode,latitude+"",longitude+"");
                    if(result)
                    {
                        Toast.makeText(MapsActivity.this, "Location Successfully updated", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }



                }
        });



        // Add a marker in Sydney and move the camera

    }

}
