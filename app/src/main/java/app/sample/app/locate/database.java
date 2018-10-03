package app.sample.app.locate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "location.db";
    public static final String TABLE_NAME = "UserLocations";
    public static final String TABLE_NAME2 = "SearchedLoacations";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "ADDRESS";
    public static final String COL_3  = "LOCALITY";
    public static final String COL_4 = "CITY";
    public static final String COL_5 = "COUNTRY_NAME";
    public static final String COL_6 = "COUNTRY_CODE";
    public static final String COL_7 = "POSTALCODE";
    public static final String COL_8 = "LATITUDE";
    public static final String COL_9 = "LONGITUDE";


    public database(Context context) {
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT , ADDRESS TEXT , LOCALITY TEXT , CITY TEXT ," +
                " COUNTRY_NAME TEXT , COUNTRY_CODE TEXT ,POSTALCODE TEXT , LATITUDE TEXT ,LONGITUDE TEXT)");

        db.execSQL("CREATE TABLE "+ TABLE_NAME2 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT , ADDRESS TEXT , LOCALITY TEXT , CITY TEXT ," +
                " COUNTRY_NAME TEXT , COUNTRY_CODE TEXT ,POSTALCODE TEXT , LATITUDE TEXT ,LONGITUDE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
    }

    public boolean insert(String address , String locality , String city , String countryname , String code,String postalcode, String latitude , String longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2,address);
        cv.put(COL_3,locality);
        cv.put(COL_4,city);
        cv.put(COL_5,countryname);
        cv.put(COL_6,code);
        cv.put(COL_7,postalcode);
        cv.put(COL_8,latitude);
        cv.put(COL_9,longitude);

        long result = db.insert(TABLE_NAME , null , cv);
        db.close();
        if(result == -1)
        {
            return false;
        }else
            return true;


    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
    }

    public boolean inserttotable2(String address , String locality , String city , String countryname , String code ,String postalcode, String latitude , String longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2,address);
        cv.put(COL_3,locality);
        cv.put(COL_4,city);
        cv.put(COL_5,countryname);
        cv.put(COL_6,code);
        cv.put(COL_7,postalcode);
        cv.put(COL_8,latitude);
        cv.put(COL_9,longitude);

        long result = db.insert(TABLE_NAME2 , null , cv);
        db.close();
        if(result == -1)
        {
            return false;
        }else
            return true;


    }

    public Cursor getDatafromtable2()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ TABLE_NAME2, null);
    }

}
