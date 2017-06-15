package com.example.parking;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class ParkingProvider extends ContentProvider {
    private static final String LOG_TAG = "ParkingProvider";  
    private static final String DB_NAME = "parkingDB";  
    private static final String DB_TABLE = "parkings";  
    public static final Uri CONTENT_URI  = Uri.parse("content://com.example.parking.ParkingProvider");
    private SQLiteDatabase  mSqlDB;
    private DatabaseHelper  mDBHelper;
    private static final int DB_VERSION = 1; 
    private static final String DB_CREATE 
         = "create table parkings( _id integer primary key autoincrement, " +   
            "licenseplate varchar(20), cartype varchar(20), parkingtype varchar(20), " +
            "locationnumber integer,starttime varchar(50),leavetime  varchar(50),expense  varchar(20), " +
            "paymentpattern varchar(20));";  
    static final String KEY_ROWID = "_id";  
    static final String KEY_LICENSE_PLATE = "licenseplate";  
    static final String KEY_CAR_TYPE = "cartype";
    static final String KEY_PARKING_TYPE="parkingtype";
    static final String KEY_LOCATION_NUMBER="locationnumber";
    static final String KEY_START_TIME="starttime";
    static final String KEY_LEAVE_TIME="leavetime";
    static final String KEY_EXPENSE="expense";
    static final String KEY_PAYMENT_PATTERN="paymentpattern";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建用于存储数据的表
        db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
    
    @Override
    public boolean onCreate() {
    	mDBHelper = new DatabaseHelper(getContext());
        return (mDBHelper == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
    	mSqlDB = mDBHelper.getWritableDatabase();
        long rowId = mSqlDB.insert(DB_TABLE, "", contentvalues);
        if (rowId > 0) {
            Uri rowUri = ContentUris.appendId(CONTENT_URI.buildUpon(), rowId).build();
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        qb.setTables(DB_TABLE);
        Cursor cursor = qb.query(db, projection, selection, null, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
    
    @Override
    public int update(Uri uri, ContentValues contentvalues, String selection, String[] selectionArgs) {
    	mSqlDB = mDBHelper.getWritableDatabase();
    	return mSqlDB.update(DB_TABLE, contentvalues, selection, null);  
    }
    
    @Override
    public String getType(Uri uri) {
        return null;
    }
    
}
