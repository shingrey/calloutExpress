package unprogramador.com.calloutexpress.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by CesarFlores on 16-Jun-17.
 */

public class Querys {
    public String select(SqliteHelper cdb, String number){
        SQLiteDatabase db = cdb.getWritableDatabase();
        String codigo="";
        if (db != null){
            String[] campos = new String[] {SqliteHelper.COLUMN_NAME};
            String[] args = new String[] {number};

            Cursor c = db.query(SqliteHelper.TABLE_PROFILES, campos, SqliteHelper.COLUMN_NUMBER+"=?", args, null, null, null);
            //aseguramos si hay registro
            if (c.moveToFirst()) {
                codigo= c.getString(0);

            }else{
                codigo="";
            }
        }
        return codigo;
    }
    public boolean insert(SqliteHelper cdb, String name, String phone){
        SQLiteDatabase db = cdb.getWritableDatabase();
        if (db != null){
            String select = select(cdb,phone);
            if(select != ""){
                deleteContact(cdb,phone);
            }
            ContentValues NewProfile = new ContentValues();
            NewProfile.put(SqliteHelper.COLUMN_NAME, name);
            NewProfile.put(SqliteHelper.COLUMN_NUMBER, phone);
            db.insert(SqliteHelper.TABLE_PROFILES, null, NewProfile);

            return true;
        }


        return false;
    }

    public boolean deleteContact(SqliteHelper cdb, String phone){
        SQLiteDatabase db = cdb.getWritableDatabase();
        if (db != null){
            String where = SqliteHelper.COLUMN_NUMBER+"='"+phone+"'";
            db.delete(SqliteHelper.TABLE_PROFILES, where , null);
            return true;
        }
        db.close();
        return false;

    }

    public ArrayList<ArrayList<String>> selectnumberblock(SqliteHelper cdb){
        SQLiteDatabase db = cdb.getWritableDatabase();
        ArrayList<ArrayList<String>> contact = new ArrayList<ArrayList<String>>();

        if (db != null){
            String[] campos = new String[] {SqliteHelper.COLUMN_ID, SqliteHelper.COLUMN_NAME, SqliteHelper.COLUMN_NUMBER};

            Cursor c = db.query(SqliteHelper.TABLE_PROFILES, campos, null,null, null, null, null);
            //aseguramos si hay registro
            int idx = 0;
            if (c.moveToFirst()) {
                do {
                    contact.add(new ArrayList<String>());
                    for (int a=0; a < 3; a++){
                        contact.get(idx).add(c.getString(a));
                    }
                    idx++;

                }while (c.moveToNext());

            }
        }
        db.close();
        return contact;
    }

}
