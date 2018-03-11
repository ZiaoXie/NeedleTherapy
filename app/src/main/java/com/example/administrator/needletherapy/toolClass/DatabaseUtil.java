package com.example.administrator.needletherapy.toolClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.administrator.needletherapy.R;


/**
 * copy数据库到apk包
 *
 * @author NGJ
 *
 */
public class DatabaseUtil {

    private Context context;
    private String dbName;// 数据库的名字
    private static String DATABASE_PATH;// 数据库在手机里的路径

    public DatabaseUtil(Context context,String dbName) {
        this.context = context;
        this.dbName=dbName;
        String packageName = context.getPackageName();
        DATABASE_PATH="/data/data/"+packageName+"/databases/";
    }

    /**
     * 判断数据库是否存在
     *
     * @return false or true
     */
    public boolean checkDataBase() {
        SQLiteDatabase db = null;
        try {
            String databaseFilename = DATABASE_PATH + dbName;
            db = SQLiteDatabase.openDatabase(databaseFilename, null,SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        if (db != null) {
            db.close();
        }
        return db != null ? true : false;
    }

    /**
     * 复制数据库到手机指定文件夹下
     *
     * @throws IOException
     */
    public void copyDataBase() throws IOException {
        String databaseFilenames = DATABASE_PATH + dbName;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
            dir.mkdir();
        FileOutputStream os = new FileOutputStream(databaseFilenames);// 得到数据库文件的写入流
        InputStream is = context.getResources().openRawResource(R.raw.needle);// 得到数据库文件的数据流
        byte[] buffer = new byte[8192];
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            os.write(buffer, 0, count);
            os.flush();
        }
        is.close();
        os.close();
    }
}