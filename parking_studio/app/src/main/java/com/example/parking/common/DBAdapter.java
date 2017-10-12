package com.example.parking.common;

import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.SQLException;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.util.Log;  
  
public class DBAdapter {  
  
    static final String KEY_ROWID = "_id";  
    static final String KEY_LICENSE_PLATE = "licenseplate";  
    static final String KEY_CAR_TYPE = "cartype";
    static final String KEY_PARKING_TYPE="parkingtype";
    static final String KEY_LOCATION_NUMBER="locationnumber";
    static final String KEY_START_TIME="starttime";
    static final String KEY_LEAVE_TIME="leavetime";
    static final String KEY_EXPENSE="expense";
    static final String KEY_PAYMENT_PATTERN="paymentpattern";
    static final String KEY_ARRIVING_IMAGE="arrivingimage";
    static final String TAG = "DBAdapter";  
      
    static final String DATABASE_NAME = "parkingDB";  
    static final String DATABASE_PARKING_TABLE = "parkings";  
    static final int DATABASE_VERSION = 1;  
      
    static final String DATABASE_CREATE =   
            "create table parkings( _id integer primary key autoincrement, " +   
            "licenseplate varchar(20), cartype varchar(20), parkingtype varchar(20), " +
            "locationnumber integer,starttime varchar(50),leavetime  varchar(50),expense  varchar(20), " +
            "paymentpattern varchar(20),arrivingimage blob);";  
    final Context context;  
      
    DatabaseHelper DBHelper;  
    SQLiteDatabase db;  
      
    public DBAdapter(Context cxt)  
    {  
        this.context = cxt;  
        DBHelper = new DatabaseHelper(context);  
    }  
      
    private static class DatabaseHelper extends SQLiteOpenHelper  
    {  
  
        DatabaseHelper(Context context)  
        {  
            super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        }  
        @Override  
        public void onCreate(SQLiteDatabase db) {   
            try  
            {  
                db.execSQL(DATABASE_CREATE);  
                android.util.Log.d("yifan","DATABASE_CREATE" );
            }  
            catch(SQLException e)  
            {  
                e.printStackTrace();  
            }  
        }  
  
        @Override  
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
            Log.wtf(TAG, "Upgrading database from version "+ oldVersion + "to "+  
             newVersion + ", which will destroy all old data");  
            db.execSQL("DROP TABLE IF EXISTS parkings");  
            onCreate(db);  
        }  
    }  
      
    //open the database  
    public DBAdapter open() throws SQLException  
    {  
        db = DBHelper.getWritableDatabase();  
        return this;  
    }

    //close the database  
    public void close()  
    {  
        DBHelper.close();  
    }  

    //insert parking information  into the database  
    public long insertParking(String licensePlate, String carType, String parkingType, int locationNumber,String startTime,
    		String leaveTime, String expense, String paymentPattern,byte[] arrivingImage)  
    {  
        ContentValues initialValues = new ContentValues();  
        initialValues.put(KEY_LICENSE_PLATE, licensePlate);  
        initialValues.put(KEY_CAR_TYPE, carType);
        initialValues.put(KEY_PARKING_TYPE, parkingType);
        initialValues.put(KEY_LOCATION_NUMBER, locationNumber);
        initialValues.put(KEY_START_TIME, startTime);
        initialValues.put(KEY_LEAVE_TIME, leaveTime);
        initialValues.put(KEY_EXPENSE, expense);
        initialValues.put(KEY_PAYMENT_PATTERN, paymentPattern);
        initialValues.put(KEY_ARRIVING_IMAGE, arrivingImage);
        return db.insert(DATABASE_PARKING_TABLE, null, initialValues);  
    }

    //delete a particular parking information
    public boolean deleteParking(long rowId)  
    {  
        return db.delete(DATABASE_PARKING_TABLE, KEY_ROWID + "=" +rowId, null) > 0;  
    }

    //get all the parking information 
    public Cursor getAllParkings()  
    {  
        return db.query(DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID,KEY_LICENSE_PLATE,KEY_CAR_TYPE,
        		KEY_PARKING_TYPE,KEY_LOCATION_NUMBER,KEY_START_TIME,KEY_LEAVE_TIME,KEY_LEAVE_TIME,
        		KEY_EXPENSE,KEY_PAYMENT_PATTERN,KEY_ARRIVING_IMAGE}, null, null, null, null, null);  
    }

    //get a particular parking  information
    public Cursor getParking(long rowId) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID,KEY_LICENSE_PLATE,KEY_CAR_TYPE,
                		KEY_PARKING_TYPE,KEY_LOCATION_NUMBER,KEY_START_TIME,KEY_LEAVE_TIME,
                		KEY_EXPENSE,KEY_PAYMENT_PATTERN,KEY_ARRIVING_IMAGE}, KEY_ROWID + "=" + rowId, null, null, null, null, null);  
        if (mCursor != null)  
            mCursor.moveToFirst();  
        return mCursor;  
    }

    //get a particular parking  information
    public Cursor getParkingByLocationNumber(int locationNumber) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID,KEY_LICENSE_PLATE,KEY_CAR_TYPE,
                		KEY_PARKING_TYPE,KEY_LOCATION_NUMBER,KEY_START_TIME,KEY_LEAVE_TIME,
                		KEY_EXPENSE,KEY_PAYMENT_PATTERN,KEY_ARRIVING_IMAGE}, KEY_LOCATION_NUMBER + "=" +"\"" +locationNumber + "\"", null, null, null, KEY_ROWID + " DESC", null);  
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;  
    }

    //get a particular parking  information
    public Cursor getParkingByLicensePlate(String licenseNumber) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID,KEY_LICENSE_PLATE,KEY_CAR_TYPE,
                		KEY_PARKING_TYPE,KEY_LOCATION_NUMBER,KEY_START_TIME,KEY_LEAVE_TIME,
                		KEY_EXPENSE,KEY_PAYMENT_PATTERN,KEY_ARRIVING_IMAGE},KEY_LICENSE_PLATE + " = " + "\"" +licenseNumber + "\"", null, null, null, KEY_ROWID + " DESC", null);  
        if (mCursor != null){
        	mCursor.moveToFirst();  	
        }
        return mCursor;  
    }

    //get a particular parking  information
    public Cursor getParkingByStartTime(String time) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(true, DATABASE_PARKING_TABLE, new String[]{ KEY_ROWID,KEY_LICENSE_PLATE,KEY_CAR_TYPE,
                		KEY_PARKING_TYPE,KEY_LOCATION_NUMBER,KEY_START_TIME,KEY_LEAVE_TIME,
                		KEY_EXPENSE,KEY_PAYMENT_PATTERN,KEY_ARRIVING_IMAGE},KEY_START_TIME + " LIKE " + "\"" +time + "\"", null, null, null, KEY_ROWID + " DESC", null);  
        if (mCursor != null){
        	mCursor.moveToFirst();  	
        }
        return mCursor;  
    }

    //updates a parking information  
    public boolean updateParking(long rowId, String licensePlate, String carType, String parkingType, int locationNumber,String startTime,
    		String leaveTime, String expense, String paymentPattern,byte[] arrivingImage)  
    {  
        ContentValues values = new ContentValues();  
        values.put(KEY_LICENSE_PLATE, licensePlate);  
        values.put(KEY_CAR_TYPE, carType);
        values.put(KEY_PARKING_TYPE, parkingType);
        values.put(KEY_LOCATION_NUMBER, locationNumber);
        values.put(KEY_START_TIME, startTime);
        values.put(KEY_LEAVE_TIME, leaveTime);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_PAYMENT_PATTERN, paymentPattern);
        values.put(KEY_ARRIVING_IMAGE, arrivingImage);
        return db.update(DATABASE_PARKING_TABLE, values, KEY_ROWID + "=" +rowId, null) > 0;  
    }  

    //updates a parking information  
    public boolean updateParking(long rowId, String leaveTime, String expense, String paymentPattern)  
    {  
        ContentValues values = new ContentValues();  
        values.put(KEY_LEAVE_TIME, leaveTime);
        values.put(KEY_EXPENSE, expense);
        values.put(KEY_PAYMENT_PATTERN, paymentPattern);
        return db.update(DATABASE_PARKING_TABLE, values, KEY_ROWID + "=" +rowId, null) > 0;  
    } 
} 