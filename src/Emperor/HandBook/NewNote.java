package Emperor.HandBook;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
 * Created by Emperor on 15.04.14.
 */
public class NewNote extends Activity implements View.OnClickListener {

    DBHelper dbHelper;
    DatePicker in_datePicker;
    EditText in_title, in_description;

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

        LinearLayout ta = (LinearLayout) findViewById(R.id.tagsList);
        for (int i = 0; i < 20; i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(String.valueOf(i)); //TODO a tag name
            ta.addView(cb);
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
        ContentValues cv = new ContentValues();
        cv.put("Title", in_title.getText().toString());
        cv.put("Desc", in_description.getText().toString());
        cv.put("Date", getDateString());
        db.insert("Notes", null, cv);
    }

    private String getDateString() {
        return String.valueOf(in_datePicker.getYear()) + "-" + (in_datePicker.getMonth() + 1) + "-" + in_datePicker.getDayOfMonth();
    }
}