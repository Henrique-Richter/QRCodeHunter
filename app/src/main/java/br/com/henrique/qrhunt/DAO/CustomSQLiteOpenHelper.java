package br.com.henrique.qrhunt.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Henrique on 27/05/2017.
 */

public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = " _id ";
    public static final String TABLE_GAME = "games";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_FOUND = "found";
    public static final String COLUMN_CODE = "code";

    private static final String DATABASE_NAME = "qrcode.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = " create table "
    + TABLE_GAME + "(" + COLUMN_ID
    + " integer primary key autoincrement , " + COLUMN_NAME
    + " text not null , " + COLUMN_QUANTITY
    + " integer not null , "+ COLUMN_FOUND
    + " text , " + COLUMN_CODE
    + " text not null UNIQUE);";

    public CustomSQLiteOpenHelper ( Context context ) {
        super ( context , DATABASE_NAME , null , DATABASE_VERSION );
    }
    @Override
    public void onCreate ( SQLiteDatabase database ) {
        database.execSQL(DATABASE_CREATE);
    }


    @Override
     public void onUpgrade ( SQLiteDatabase db , int oldVersion , int newVersion ) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_GAME);
        onCreate(db);
    }
}
