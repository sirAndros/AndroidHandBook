package Emperor.HandBook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
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

public class Main extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int CM_DELETE_ID = 1;
    long backPressed;

    ItemTypes itemType;
    public static DBHelper dbHelper;
    public static Main mainContext;
    private SimpleCursorAdapter notesCursorAdapter;
    private SimpleCursorAdapter tagsCursorAdapter;
    private MyFragmentPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        itemType = ItemTypes.Note;

        dbHelper = new DBHelper(this);
        mainContext = this;

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
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
                        RefreshNotes();
                        break;
                    case Tag:
                        RefreshTags();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }



    private void RefreshTags() {
        if (tagsCursorAdapter == null)
            CreateTags();
    }

    private void RefreshNotes() {
        if (notesCursorAdapter == null)
            CreateNotes();
    }

    private void CreateTags() {
        tagsCursorAdapter = new SimpleCursorAdapter(this, R.layout.tag_item, null, new String[] {"Title"},
                new int[]{R.id.tagName}, 0);
        ListView lvData = (ListView) ((PageFragment)pagerAdapter.getItem(1)).tags.findViewById(R.id.tagsList);
        lvData.setAdapter(tagsCursorAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);
        lvData.setOnItemClickListener(this);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(1, null, this);
    }

    private void CreateNotes() {
        notesCursorAdapter = new SimpleCursorAdapter(this, R.layout.note_item, null, new String[] {"Title", "Date", "Desc"},
                new int[]{R.id.tv_title, R.id.tv_date, R.id.tv_desc}, 0);
        ListView lvData = (ListView) ((PageFragment)pagerAdapter.getItem(0)).notes.findViewById(R.id.notesList);
        lvData.setAdapter(notesCursorAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);
        lvData.setOnItemClickListener(this);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);
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





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        intent.putExtra("ROW_ID", id);
        startActivity(intent);
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
            dbHelper.getWritableDatabase().delete(itemType == ItemTypes.Note ? DBHelper.NOTES_TABLE : DBHelper.TAGS_TABLE, "_id = " + acmi.id, null);
            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(itemType == ItemTypes.Note ? 0 : 1).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Loader<Cursor> loader = getSupportLoaderManager().getLoader(itemType == ItemTypes.Note ? 0 : 1);
        if (loader != null)
            loader.forceLoad();
    }

    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, dbHelper, itemType);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (itemType) {
            case Note:
                notesCursorAdapter.swapCursor(cursor);
                break;
            case Tag:
                tagsCursorAdapter.swapCursor(cursor);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
