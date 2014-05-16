package Emperor.HandBook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "NoteBookDB", null, 1);
    }

    public final static String NOTES_TABLE = "Notes";
    public final static String NOTES_TAGS_TABLE = "NotesTags";
    public final static String TAGS_TABLE = "Tags";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + NOTES_TABLE + " ("
                + "_id integer primary key autoincrement,"
                + "Title text,"
                + "Desc text,"
                + "Date text"
                + ");");
        sqLiteDatabase.execSQL("create table " + NOTES_TAGS_TABLE + " ("
                + "Note int,"
                + "Tag int,"
                + "UNIQUE(Note, Tag)"
                + ");");
        sqLiteDatabase.execSQL("create table " + TAGS_TABLE + " ("
                + "_id integer primary key autoincrement,"
                + "Title text,"
                + "Desc text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }
}
