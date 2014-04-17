package Emperor.HandBook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class Main extends FragmentActivity {
    public static Activity getMainActivity() {
        return mainActivity;
    }
    private static Activity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mainActivity = this;

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        FragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);


//        Intent intent = new Intent(this, NewNote.class);
//        startActivity(intent);
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
