package lilun.com.pension.module.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
*搜索记录数据库操作helper
*@author yk
*create at 2017/3/28 15:44
*email : yk_developer@163.com
*/
public class RecordDbOpenHelper extends SQLiteOpenHelper {

    public RecordDbOpenHelper(Context context) {
        super(context, "record.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table records(id integer primary key autoincrement,searchStr varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
