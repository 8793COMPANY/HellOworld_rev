package com.corporation.helloworld.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RecordDB extends SQLiteOpenHelper {

    public RecordDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE record(_id integer primary key," +"date TEXT, senddata TEXT,identify interger not null);");
        db.execSQL("CREATE TABLE information(_id integer primary key   ," +"firstcall TEXT default \'없음\'  , lastcall TEXT  default \'없음\' ,screenopen integer  default \'0\' ,screencheck boolean default 1 ,activitycount integer  default \'0\', callsize integer,batterycheck boolean );");
        db.execSQL("INSERT INTO information VALUES(1,'없음','없음',0,1,0,0,1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }







}
