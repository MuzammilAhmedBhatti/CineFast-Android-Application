package com.example.cinefast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SnackDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_SNACKS = "snacks";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image";

    private Context context;

    public SnackDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create snacks table
        String createTable = "CREATE TABLE " + TABLE_SNACKS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_PRICE + " REAL, "
                + COL_IMAGE + " TEXT)";
        db.execSQL(createTable);

        // Insert initial snack data
        insertSnack(db, "Medium Soft Popcorn", 8.99, "popcorn");
        insertSnack(db, "Large Caramel Nachos", 7.99, "nachos");
        insertSnack(db, "Large Soft Drink", 5.99, "soft_drink");
        insertSnack(db, "Candy Mix", 6.99, "candy");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertSnack(SQLiteDatabase db, String name, double price, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);
        db.insert(TABLE_SNACKS, null, values);
    }

    public ArrayList<Snack> getAllSnacks() {
        ArrayList<Snack> snackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SNACKS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));

                Snack snack = new Snack(id, name, price, imageName);

                // Map image name to resource ID
                int imageResId = context.getResources().getIdentifier(
                        imageName, "drawable", context.getPackageName());
                if (imageResId == 0) {
                    imageResId = R.drawable.img; // fallback
                }
                snack.setImageResId(imageResId);

                snackList.add(snack);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return snackList;
    }
}
