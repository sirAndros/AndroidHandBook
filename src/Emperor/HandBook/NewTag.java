package Emperor.HandBook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Emperor on 15.04.14.
 */
public class NewTag extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private int id;
    private SimpleCursorAdapter notesCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag);

        id = getIntent().getExtras().getInt("ROW_ID", -1);

        notesCursorAdapter = new SimpleCursorAdapter(this, R.layout.note_item, null, new String[] {"Title", "Date", "Desc"},
                new int[]{R.id.tv_title, R.id.tv_date, R.id.tv_desc}, 0);
        ListView lvData = (ListView)findViewById(R.id.relatedNotes);
        lvData.setAdapter(notesCursorAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);
        lvData.setOnItemClickListener(Main.mainContext);

        // создаем лоадер для чтения данных
        Main.mainContext.getSupportLoaderManager().initLoader(0, null, Main.mainContext);
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
            Main.mainContext.getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}