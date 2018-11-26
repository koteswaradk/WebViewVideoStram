package servicetutorial.service;

import android.database.Cursor;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GpsMapsLocationsActivity extends FragmentActivity implements OnMapReadyCallback {
    private String TAG=getClass().getSimpleName();
    GPSAdapter adapter;
    String latitude, langitude, locality;
    private ArrayList<GPSModel> gpsModelArrayList = new ArrayList<GPSModel>();
    ListView listView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_maps_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listView=(ListView) findViewById(R.id.list_gpsdata);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cursor = getContentResolver().query(GPSProvider.CONTENT_GPS_MONITOR_URI, null, null, null, null, null);
        }
        gpsModelArrayList.clear();
        if (cursor.moveToFirst()) {
            do{
                String address= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_ADDRESS));
                String area= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_AREA));
                String locality= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LOCALITY));
                 latitude= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LATITUDE));
                 langitude= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LANGITUDE));
                GPSModel gpsModel=new GPSModel();
                gpsModel.setAddress(address);
                gpsModel.setArea(area);
                gpsModel.setLocality(locality);
                gpsModel.setLatitude(latitude);
                gpsModel.setLangitude(langitude);

                gpsModelArrayList.add(gpsModel);
                adapter=new GPSAdapter(this,gpsModelArrayList);
                listView.setAdapter(adapter);

            } while (cursor.moveToNext());
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

        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cursor = getContentResolver().query(GPSProvider.CONTENT_GPS_MONITOR_URI, null, null, null, null, null);
        }
        if (cursor.moveToFirst()) {
            do{
                String address= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_ADDRESS));
                String area= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_AREA));
                locality= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LOCALITY));
                latitude= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LATITUDE));
                langitude= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LANGITUDE));
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(langitude))).title(locality+" "+area+" "+address));

                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude), Double.parseDouble(langitude)), 11));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude), Double.parseDouble(langitude)), 11));


            } while (cursor.moveToNext());
        }

    }



}
