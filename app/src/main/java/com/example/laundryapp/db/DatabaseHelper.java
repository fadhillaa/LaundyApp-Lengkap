package com.example.laundryapp.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.laundryapp.model.ModelHistory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_laundry";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "checkout_table";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TOTAL_ITEMS = "total_items";
    private static final String COLUMN_TOTAL_PRICE = "total_price";

    private static final String TABLE_USER = "user_table";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHECKOUT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_TOTAL_ITEMS + " INTEGER," +
                COLUMN_TOTAL_PRICE + " INTEGER," +
                COLUMN_USER_ID + " INTEGER)";

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_USER_EMAIL + " TEXT," +
                COLUMN_USER_PASSWORD + " TEXT)";

        db.execSQL(CREATE_CHECKOUT_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void insertCheckoutData(String title, int totalItems, int totalPrice, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        int userId = getUserIdByEmail(userEmail);

        if (userId != -1) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_TOTAL_ITEMS, totalItems);
            values.put(COLUMN_TOTAL_PRICE, totalPrice);
            values.put(COLUMN_USER_ID, userId);

            long result = db.insert(TABLE_NAME, null, values);
            if (result == -1) {
                Log.e("Insert Checkout", "Failed to insert checkout data");
            }
        } else {
            Log.e("Insert Checkout", "User does not exist");
        }

        db.close();
    }

    @SuppressLint("Range")
    public List<ModelHistory> getAllCheckoutData(String userEmail) {
        List<ModelHistory> checkoutDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        int userId = getUserIdByEmail(userEmail);

        Cursor cursor = null;

        try {
            if (userId != -1) {
                String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ?";
                cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

                while (cursor != null && cursor.moveToNext()) {
                    ModelHistory checkoutData = new ModelHistory();
                    checkoutData.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    checkoutData.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    checkoutData.setItems(cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_ITEMS)));
                    checkoutData.setPrice(cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_PRICE)));
                    checkoutDataList.add(checkoutData);
                }
            } else {
                throw new IllegalArgumentException("User does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return checkoutDataList;
    }


    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};

        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count > 0;
    }
    @SuppressLint("Range")
    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USERNAME};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        String username = null;
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        }
        cursor.close();
        db.close();

        return username;
    }
    public void deleteItemData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    @SuppressLint("Range")
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
        }

        if (cursor != null) {
            cursor.close();
        }

        return userId;
    }


}
