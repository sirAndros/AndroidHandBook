package Emperor.HandBook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Emperor on 15.04.14.
 */
public class NewTag extends FragmentActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private long id = -1;
    private SimpleCursorAdapter notesCursorAdapter;
    private EditText tagTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);

        tagTitle = (EditText) findViewById(R.id.tagTitle);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("ROW_ID", -1);

            if (id != -1) {
                SQLiteDatabase db = Main.dbHelper.getReadableDatabase();
                Cursor cur = db.query(DBHelper.TAGS_TABLE, new String[]{"Title"}, "_id = " + String.valueOf(id), null, null, null, null);
                if (cur.moveToNext()) {
                    tagTitle.setText(cur.getString(0));
                }
            }
        }

        if (id == -1) {
            tagTitle.requestFocus();
        }

        notesCursorAdapter = new SimpleCursorAdapter(this, R.layout.note_item, null, new String[] {"Title", "Date", "Desc"},
                new int[]{R.id.tv_title, R.id.tv_date, R.id.tv_desc}, 0);
        ListView lvData = (ListView)findViewById(R.id.relatedNotes);
        lvData.setAdapter(notesCursorAdapter);

        registerForContextMenu(lvData);
        lvData.setOnItemClickListener(this);

        getSupportLoaderManager().initLoader(2, null, this);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, R.string.delete_menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            Main.dbHelper.getWritableDatabase().delete(DBHelper.NOTES_TAGS_TABLE, "Note = " + acmi.id + " and Tag = " + id, null);
            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(2).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Loader<Cursor> loader = getSupportLoaderManager().getLoader(2);
        if (loader != null)
            loader.forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new MyCursorLoader(this, Main.dbHelper, ItemTypes.NotesForTag, id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        notesCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                save();
                finish();
                break;
        }
    }

    private void save() {
        String title = tagTitle.getText().toString();
        if ("".equals(title)) {
            Toast.makeText(getBaseContext(), getString(R.string.emptyTagMessage), Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase db = Main.dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title", title);

        if (id == -1) {
            try {
                db.insert(DBHelper.TAGS_TABLE, null, cv);
            } catch (SQLiteConstraintException e) {
            }
        }
        else
            db.update(DBHelper.TAGS_TABLE, cv, "_id = " + String.valueOf(id), null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NewNote.class);
        intent.putExtra("ROW_ID", id);
        startActivity(intent);
    }
}