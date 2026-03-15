package com.example.drivertracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DriverTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_USERS = "users";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_DRIVER_NAME = "driver_name";
    private static final String COLUMN_PHONE = "phone";

    // Create table query
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_DRIVER_NAME + " TEXT,"
            + COLUMN_PHONE + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);

        // Insert a default driver for testing
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "driver1");
        values.put(COLUMN_PASSWORD, "pass123");
        values.put(COLUMN_DRIVER_NAME, "John Driver");
        values.put(COLUMN_PHONE, "1234567890");
        db.insert(TABLE_USERS, null, values);

        values = new ContentValues();
        values.put(COLUMN_USERNAME, "driver2");
        values.put(COLUMN_PASSWORD, "pass123");
        values.put(COLUMN_DRIVER_NAME, "Jane Driver");
        values.put(COLUMN_PHONE, "0987654321");
        db.insert(TABLE_USERS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to check if user exists
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Method to register new user
    public boolean registerUser(String username, String password, String driverName, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_DRIVER_NAME, driverName);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Method to get driver name
    public String getDriverName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_DRIVER_NAME + " FROM " + TABLE_USERS
                + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        String driverName = "";
        if (cursor.moveToFirst()) {
            driverName = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return driverName;
    }
}
