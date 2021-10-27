package com.vaccine;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME="userData.db";
	public static final String TABLE_NOW="table_user";
	public static final String COL_ID="ID";
	public static final String COL_NAME="NAME";
	public static final String COL_DATE="DATE";
	public static final String COL_PHONE="PHONE";

	String create=" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DATE TEXT,PHONE TEXT)";

	public DatabaseHelper(Context c){
		super(c,DATABASE_NAME,null,1);
	}
	@Override 
	public void onCreate(SQLiteDatabase db){
		db.execSQL("create table "+TABLE_NOW+create);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOW);
		onCreate(db);
	}
	public boolean inert_data(String name,String date,String phone){
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cnt=new ContentValues();
		cnt.put(COL_NAME,name);
		cnt.put(COL_DATE,date);
		cnt.put(COL_PHONE,phone);

	    long now=db.insert(TABLE_NOW,null,cnt);

		return now != -1;
    }
	public boolean update_data(String id,String name,String date,String phone){
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues cnt=new ContentValues();
		cnt.put(COL_ID,id);
		cnt.put(COL_NAME,name);
		cnt.put(COL_DATE,date);
		cnt.put(COL_PHONE,phone);

		long up=db.update(TABLE_NOW,cnt,"ID=?",new String[]{id});
		return up != -1;
	}
	public Cursor get_data(){
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor c=db.rawQuery("select*from "+TABLE_NOW,null);
		return c;
	}
	public Integer delete_data(String id){
		SQLiteDatabase db=this.getWritableDatabase();
		return db.delete(TABLE_NOW,"ID=?",new String[]{id});
	}
}
