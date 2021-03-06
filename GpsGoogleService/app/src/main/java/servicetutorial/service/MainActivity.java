package servicetutorial.service;

import android.*;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.renderscript.Double2;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private String TAG=getClass().getSimpleName();
    Button btn_start;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    TextView tv_latitude, tv_longitude, tv_address,tv_area,tv_locality;

    Double latitude,longitude;
    Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = (Button) findViewById(R.id.btn_start);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_latitude = (TextView) findViewById(R.id.tv_latitude);
        tv_longitude = (TextView) findViewById(R.id.tv_longitude);
        tv_area = (TextView)findViewById(R.id.tv_area);
        tv_locality = (TextView)findViewById(R.id.tv_locality);
        geocoder = new Geocoder(this, Locale.getDefault());

       /* Intent intent = new Intent(getApplicationContext(), GoogleService.class);
        startService(intent);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        fn_permission();
        PendingIntent service = null;
        Intent intentForService = new Intent(this.getApplicationContext(), GoogleService.class);
        final AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        if (service == null) {
            service = PendingIntent.getService(this, 0,
                    intentForService, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTime().getTime(), 60, service);
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            latitude = Double.valueOf(intent.getStringExtra("latutide"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                tv_area.setText(addresses.get(0).getAdminArea());
                tv_locality.setText(stateName);
                tv_address.setText(countryName);

                ContentValues selectedValues = new ContentValues();
                selectedValues.put(GPSDBHelper.KEY_ADDRESS,countryName);
                selectedValues.put(GPSDBHelper.KEY_LOCALITY, stateName);
                selectedValues.put(GPSDBHelper.KEY_AREA, addresses.get(0).getAdminArea());
                selectedValues.put(GPSDBHelper.KEY_LATITUDE, String.valueOf(latitude));
                selectedValues.put(GPSDBHelper.KEY_LANGITUDE, String.valueOf(longitude));

                Uri selectedUri = getContentResolver().insert(GPSProvider.CONTENT_GPS_MONITOR_URI, selectedValues);
                if (selectedUri!=null){
                    if (ContentUris.parseId(selectedUri)>0);
                       // Log.i(TAG,"insertion success");
                }else{
                   // Log.i(TAG,"insertion fails");
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            }


            tv_latitude.setText(latitude+"");
            tv_longitude.setText(longitude+"");
            tv_address.getText();


        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            startActivity(new Intent(MainActivity.this,GpsMapsLocationsActivity.class));


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), GoogleService.class);
        stopService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


}
