package servicetutorial.service;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class DisplayGPSData extends AppCompatActivity  {
    private String TAG=getClass().getSimpleName();
    GPSAdapter adapter;
    private ArrayList<GPSModel> gpsModelArrayList = new ArrayList<GPSModel>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_gpsdata);
        listView=(ListView) findViewById(R.id.list_gpsdata);

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
                String latitude= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LATITUDE));
                String langitude= cursor.getString(cursor.getColumnIndex(GPSDBHelper.KEY_LANGITUDE));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }



}
