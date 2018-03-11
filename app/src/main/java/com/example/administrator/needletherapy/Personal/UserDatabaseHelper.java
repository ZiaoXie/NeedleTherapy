package com.example.administrator.needletherapy.Personal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class UserDatabaseHelper extends SQLiteOpenHelper {
	public static final String  TABLE_NAME="User";
	public static final String CREAT_RECORD="create table IF NOT EXISTS "+
			TABLE_NAME+
            "(id int primary key not null," +
            "password text," +
			"nickname text," +
			"age int,"+
			"birthday text,"+
			"sex text)";
    private Context mContext;
	public UserDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAT_RECORD);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
