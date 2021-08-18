package com.example.leafdiseaseidentification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DB_NAME ="register.db"; //DATABSE NAME
    public static final String TABLE_NAME ="registeruser";      //TABLE NAME OF REGISTERED USERS
    public static final String COLUMN_1 ="ID";      //COLUMN ID
    public static final String COLUMN_2 ="username";        //uSERNAME COLUMN
    public static final String COLUMN_3 ="password";            //PASSWORD COLUMN

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY  KEY AUTOINCREMENT, username TEXT, password TEXT)");
    }


//METHOD FOR ADDING USER INTO THE USERNAME COLUMN AND DATABASE
    public long addUser(String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username",user);
        contentValues.put("Password",password);
        long res = db.insert("Registeruser",null,contentValues);
        db.close();
        return  res;
    }
        //DELETE THE TABLE IF IT EXISTS ALREADY OR CREATED
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
//METHOD FOR CHECKING/VALIDATING THE USER IN THE DATABASE
    public boolean checkUser(String username, String password){
        String[] columns = {COLUMN_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_2 + "=?" + " and " + COLUMN_3 + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
//CLOSING THE DATABASE AFTER CHECKING
        if(count>0)
            return  true;
        else
            return  false;
    }
}
