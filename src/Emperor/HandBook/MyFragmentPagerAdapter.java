package Emperor.HandBook;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    Context context;
    HashMap<Integer, PageFragment> pageFragments;

    public MyFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        pageFragments = new HashMap<Integer, PageFragment>();
    }

    @Override
    public Fragment getItem(int position) {
        if(!pageFragments.containsKey(position))
            pageFragments.put(position, PageFragment.newInstance(position));
        return pageFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_notes);
            case 1:
                return context.getString(R.string.tab_tags);
        }
        return "";
    }

    @Override
    public int getCount() {
        return 2;
    }

}
