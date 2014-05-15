package Emperor.HandBook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.CursorLoader;

import java.util.concurrent.TimeUnit;

public class MyCursorLoader extends CursorLoader {
    DBHelper dbHelper;
    ItemTypes itemTypes;
    long id = -1;

    public MyCursorLoader(Context context, DBHelper db, ItemTypes itemTypes) {
        super(context);
        this.dbHelper = db;
        this.itemTypes = itemTypes;
    }

    public MyCursorLoader(Context context, DBHelper dbHelper, ItemTypes itemTypes, long id) {
        this(context, dbHelper, itemTypes);
        this.id = id;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;

        switch (itemTypes) {
            case Tag:
                cursor = getAllTags();
                break;
            case Note:
                cursor = getAllNotes();
                break;
            case TagsForNote:
                cursor = getTagsForNote();
                break;
            case NotesForTag:
                cursor = getNotesForTag();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    private Cursor getNotesForTag() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DBHelper.NOTES_TABLE + " join " + DBHelper.NOTES_TAGS_TABLE + " on Note = _id", null, "_id = " + id, null, null, null, null);
    }

    private Cursor getTagsForNote() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DBHelper.TAGS_TABLE + " join " + DBHelper.NOTES_TAGS_TABLE + " on Tag = _id", null, "_id = " + id, null, null, null, null);
    }

    private Cursor getAllTags() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DBHelper.TAGS_TABLE, null, null, null, null, null, null);
    }

    private Cursor getAllNotes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DBHelper.NOTES_TABLE, null, null, null, null, null, null);
    }
}
