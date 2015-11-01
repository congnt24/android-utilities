package database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by cong on 8/29/2015.
 */
public class SQLiteHelper extends BaseSQLiteHelper {
    private SQLiteDatabase sqLiteDatabase;
    private static SQLiteHelper instance;

    public SQLiteHelper(Context context) {
        super(context);
    }

    @Override
    public boolean openDatabase(String dbname) {
        File file = new File(context.getFilesDir()+"/"+dbname);
        if (!file.exists()) {
            //Try to get db from assets foder
            copyDataBase(dbname);
        }
        sqLiteDatabase = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READONLY);
        return sqLiteDatabase.isOpen();
    }

    @Override
    public Cursor queryAll(String tableName) {
        return sqLiteDatabase.query(tableName, null, null, null, null, null, null);
    }

    @Override
    public Cursor queryRandom(String tableName, int amount) {
        return sqLiteDatabase.query(tableName, null, null, null, null, null, "RANDOM()", String.valueOf(amount));
    }

    @Override
    public void insert(String tableName, ContentValues values) {
        sqLiteDatabase.insert(tableName, null, values);
    }

    @Override
    public List getLikeWord(String tableName, String word, int limit) {
//        return sqLiteDatabase.rawQuery("SELECT word FROM ? WHERE word LIKE \"?%\" LIMIT ?",new String[]{tableName, word, String.valueOf(limit)});
        return null;
    }

    @Override
    public Cursor getOneRow(String tableName, String column, String arg) {
        return null;
    }

    public static SQLiteHelper getInstance(Context context){
        if (instance == null){
            instance = new SQLiteHelper(context);
        }
        return instance;
    }
}
