package unprogramador.com.calloutexpress.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CesarFlores on 15-Jun-17.
 */

public class SqliteHelper extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "phone";
    public static final String COLUMN_NAME = "name";
    public static final String DATABASE_NAME = "blocks.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_PROFILES = "profiles";
    private static final String DATABASE_CREATE = "create table "+TABLE_PROFILES+"( _id integer primary key autoincrement, "+ COLUMN_NAME +" text, " + COLUMN_NUMBER +" text not null);";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //db.execSQL("DROP TABLE IF EXISTS "+TABLE_PROFILES);
        onCreate(db);

    }
}
