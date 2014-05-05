package Emperor.HandBook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Main extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    long backPressed;

    ItemTypes itemType;
    public static DBHelper dbHelper;
    private SimpleCursorAdapter scAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        itemType = ItemTypes.Note;

        dbHelper = new DBHelper(this);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                itemType = ItemTypes.values()[i];

                switch (itemType) {
                    case Note:
//                        RefreshNotes();
                        break;
                    case Tag:
//                        RefreshTags();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        scAdapter = new SimpleCursorAdapter(this, R.layout.note_item, null, new String[] {"Title", "Date", "Desc"},
                new int[]{R.id.tv_title, R.id.tv_date, R.id.tv_desc}, 0);
        ListView lvData = (ListView) ((PageFragment)pagerAdapter.getItem(0)).notes.findViewById(R.id.notesList);
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void RefreshTags() {

    }

    private void RefreshNotes() {
        ListView listView = (ListView) findViewById(R.id.notesList);
//        ArrayList<String> notes = loadNotes();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.note_item, notes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.add_menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Class<?> cls = null;
                switch (itemType) {
                    case Tag:
                        cls = NewTag.class;
                        break;
                    case Note:
                        cls = NewNote.class;
                        break;
                }
                Intent intent = new Intent(this, cls);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), getString(R.string.exit_toast), Toast.LENGTH_SHORT).show();
        backPressed = System.currentTimeMillis();
    }







    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            dbHelper.getWritableDatabase().delete(DBHelper.NOTES_TABLE, "_id = " + acmi.id, null);
            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Loader<Cursor> loader = getSupportLoaderManager().getLoader(0);
        if (loader != null)
            loader.forceLoad();
    }

    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, dbHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {
        DBHelper dbHelper;

        public MyCursorLoader(Context context, DBHelper db) {
            super(context);
            this.dbHelper = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = getAllNotes();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

        private Cursor getAllNotes() {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            return db.query(DBHelper.NOTES_TABLE, null, null, null, null, null, null);
        }
    }
}
