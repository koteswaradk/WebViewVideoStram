package servicetutorial.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by koteswara on 18/06/17.
 */

public class GPSDBHelper extends SQLiteOpenHelper {
    //DB NAMES AND VERSION
    public static final String DATABASE_NAME="gps.db";
    public static final int VERSION=1;

    //TABLE NAME
    public static final String GPS_TABLE="gpsdetails";

    //COLOUMN NAMES
    public static final String KEY_ID="id";
    public static final String KEY_LATITUDE="latitude";
    public static final String KEY_LANGITUDE="langitude";
    public static final String KEY_ADDRESS="address";
    public static final String KEY_AREA="rlistatusapi";
    public static final String KEY_LOCALITY="rlistatusnpiapi";

    public static final String KEY_DEBUG=null;

    //CREATE TABLE STRING
    private static final String CREATE_TABLE_GPS_DETAILS = "CREATE TABLE "
            + GPS_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LATITUDE
            + " TEXT," + KEY_LANGITUDE + " TEXT," + KEY_ADDRESS
            + " TEXT," + KEY_AREA + " TEXT,"
            + KEY_LOCALITY + " TEXT" + ")";

    public GPSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_GPS_DETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CREATE_TABLE_GPS_DETAILS);
        onCreate(sqLiteDatabase);
    }
}
