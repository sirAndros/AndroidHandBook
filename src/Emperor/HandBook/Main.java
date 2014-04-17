package Emperor.HandBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        Intent intent = new Intent(this, NewNote.class);
        startActivity(intent);
//        TabHost tabHost = getTabHost();
//        TabHost.TabSpec tabSpec;
//
//        tabSpec = tabHost.newTabSpec("notes");
//        tabSpec.setIndicator(getString(R.string.tab_notes));
//        tabSpec.setContent(new Intent(this, NewNote.class));
//        tabHost.addTab(tabSpec);
//
//        tabSpec = tabHost.newTabSpec("tags");
//        tabSpec.setIndicator(getString(R.string.tab_tags));
//        tabSpec.setContent(new Intent(this, NewTag.class));
//        tabHost.addTab(tabSpec);
    }
}
