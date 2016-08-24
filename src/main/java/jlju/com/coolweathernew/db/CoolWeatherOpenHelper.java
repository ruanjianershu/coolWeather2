package jlju.com.coolweathernew.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 11415 on 2016/8/24.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    /**
     * PROVINCE建表语句
     */
    public static final String CREATE_PROVINCE = "CREATE TABLE Province("
            + "id integer primary key autoincrement ,"
            + "province_name text ,"
            + "province_code text)";

    /**
     * CITY建表语句
     */
    public static final String CREATE_CITY = "CREATE TABLE Province("
            + "id integer primary key autoincrement,"
            + "city_name text,"
            + "city_code text,"
            + "province_id integer)";

    /**
     * COUNTY建表语句
     */
    public static final String CREATE_COUNTY = "CREATE TABLE Province("
            + "id integer primary key autoincrement,"
            + "county_name text,"
            + "county_code text,"
            + "city_id integer)";

    public CoolWeatherOpenHelper(Context context, String name,
                                 CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
