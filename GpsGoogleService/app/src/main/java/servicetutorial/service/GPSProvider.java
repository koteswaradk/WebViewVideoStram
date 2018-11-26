package servicetutorial.service;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by koteswara on 18/06/17.
 */

public class GPSProvider extends ContentProvider{
    //AUTHORITY
    public static final String CONTENT_AUTHORITY = "com.juniper.gps.gpsmonitor";

    public static final String GPS_MONITOR_URL = "content://"+ CONTENT_AUTHORITY +"/"+ GPSDBHelper.GPS_TABLE;

    public static final Uri CONTENT_GPS_MONITOR_URI = Uri.parse(GPS_MONITOR_URL);


    //URI MATCHER ID FOR DATA RETRIVE
    static final int GPS_MONITIR = 1;
    static final int GPS_MONITOR_ID = 2;

    /**
     * Database specific constant declarations
     */
    public SQLiteDatabase db;

    //DB HELPER OBJECT DECLARATION
    GPSDBHelper DBHelper;

    private static HashMap<String, String> values;

    //URI MARTECHER
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CONTENT_AUTHORITY, GPSDBHelper.GPS_TABLE, GPS_MONITIR);
        uriMatcher.addURI(CONTENT_AUTHORITY, GPSDBHelper.GPS_TABLE+"/#", GPS_MONITOR_ID);

    }
    @Override
    public boolean onCreate() {
        DBHelper=new GPSDBHelper(getContext());

        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case GPS_MONITIR:
                qb.setTables(GPSDBHelper.GPS_TABLE);
                qb.setProjectionMap(values);
                break;
            case GPS_MONITOR_ID:
                qb.setTables(GPSDBHelper.GPS_TABLE);
                qb.appendWhere( GPSDBHelper.KEY_ADDRESS + "=?" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);

        }
        db = DBHelper.getWritableDatabase();
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        //   db.close();
        Log.d("inside query", "queried records: "+c.getCount());
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case GPS_MONITIR:
                return "vnd.android.cursor.dir/vnd.com.juniper.gps."+ GPSDBHelper.GPS_TABLE;
            case GPS_MONITOR_ID:
                return "vnd.android.cursor.dir/vnd.com.juniper.gps."+ GPSDBHelper.GPS_TABLE;
            default:
                throw new IllegalArgumentException("Invalid URI: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        db=DBHelper.getWritableDatabase();
        long rowID=0;
        /**
         * Add a new  records
         */
        switch (uriMatcher.match(uri)){
            case GPS_MONITIR:
                rowID = db.insert(GPSDBHelper.GPS_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;

        }
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(uri, rowID);

            Log.i("uri after insert",_uri.toString());
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count =0;
        db=DBHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case GPS_MONITIR:
                count = db.delete(GPSDBHelper.GPS_TABLE, null, null);
                break;
            case GPS_MONITOR_ID:
                String idStr1 = uri.getLastPathSegment();
                String where1 = GPS_MONITOR_ID + " = " + idStr1;
                if (!TextUtils.isEmpty(selection)) {
                    where1 += " AND " + selection;
                }
                count = db.delete(GPSDBHelper.GPS_TABLE, where1, selectionArgs);

                break;

            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, String wheree, String[] whereArgs) {
        int count =0;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(GPSDBHelper.GPS_TABLE);
        db=DBHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case GPS_MONITIR:
                count = db.update(GPSDBHelper.KEY_ADDRESS, contentValues, wheree, whereArgs);

                break;
            case GPS_MONITOR_ID:
                String idStr = uri.getLastPathSegment();
                String where = GPS_MONITOR_ID + " = " + idStr;
                if (!TextUtils.isEmpty(wheree)) {
                    where += " AND " + wheree;
                }
                count = db.update(GPSDBHelper.GPS_TABLE, contentValues, where, whereArgs);
                break;


            default:
                throw new IllegalArgumentException( "illegal uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
