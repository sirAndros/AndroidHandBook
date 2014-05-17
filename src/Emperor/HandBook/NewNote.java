package Emperor.HandBook;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Emperor on 15.04.14.
 */
public class NewNote extends Activity implements View.OnClickListener {

    DBHelper dbHelper;
    DatePicker in_datePicker;
    EditText in_title, in_description;
    long id = -1;
    ArrayList<TagEntity> tags = new ArrayList<TagEntity>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);

        dbHelper = Main.dbHelper;

        Button save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(this);

        Button cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        in_datePicker = (DatePicker) findViewById(R.id.datePicker);
        in_title = (EditText) findViewById(R.id.tb_title);
        in_description = (EditText) findViewById(R.id.tb_desc);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TAGS_TABLE, new String[]{"_id", "Title"}, null, null, null, null, null);
        LinearLayout ta = (LinearLayout) findViewById(R.id.tagsList);

        while (cursor.moveToNext()) {
            CheckBox cb = new CheckBox(this);
            cb.setText(cursor.getString(1));
            ta.addView(cb);
            tags.add(new TagEntity(cb, cursor.getLong(0)));
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("ROW_ID", -1);

            if (id != -1) {
                Cursor cur = db.query(DBHelper.NOTES_TABLE, new String[]{"Title", "Desc", "Date"}, "_id = " + String.valueOf(id), null, null, null, null);
                if (cur.moveToNext()) {
                    in_title.setText(cur.getString(0));
                    in_description.setText(cur.getString(1));
                    setDateToDatePicker(cur.getString(2));
                }
                cur = db.query(DBHelper.NOTES_TAGS_TABLE, new String[]{"Tag"}, "Note = " + String.valueOf(id), null, null, null, null);
                while (cur.moveToNext()) {
                    long tagID = cur.getLong(0);
                    for (TagEntity tag : tags) {
                        if (tag.id == tagID) {
                            tag.checkBox.setChecked(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                save();
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void save() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String title = in_title.getText().toString();
        if (!"".equals(title)) {
            ContentValues cv = new ContentValues();
            cv.put("Title", title);
            cv.put("Desc", in_description.getText().toString());
            cv.put("Date", getDateString());
            if (id == -1)
                id = db.insert(DBHelper.NOTES_TABLE, null, cv);
            else
                db.update(DBHelper.NOTES_TABLE, cv, "_id = " + String.valueOf(id), null);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.emptyNoteTitle), Toast.LENGTH_SHORT).show();
        }
        if (id != -1) {
            for (TagEntity tagEntity : tags) {
                if (tagEntity.checkBox.isChecked()) {
                    db.execSQL("insert or ignore into " + DBHelper.NOTES_TAGS_TABLE + "(Note,Tag) values (" + id + ", " + tagEntity.id + ")");
                } else {
                    db.delete(DBHelper.NOTES_TAGS_TABLE, "Note = " + id + " and Tag = " + tagEntity.id, null);
                }
            }
        }
    }

    private String getDateString() {
        return String.valueOf(in_datePicker.getYear()) + "-" + (in_datePicker.getMonth() + 1) + "-" + in_datePicker.getDayOfMonth();
    }

    private void setDateToDatePicker(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date =  formatter.parse(dateString);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            in_datePicker.updateDate(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    class TagEntity {
        public CheckBox checkBox;
        public long id;

        TagEntity(CheckBox checkBox, long id) {
            this.checkBox = checkBox;
            this.id = id;
        }
    }
}