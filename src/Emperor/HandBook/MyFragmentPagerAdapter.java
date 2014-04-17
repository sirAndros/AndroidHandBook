package Emperor.HandBook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return Main.getMainActivity().getString(R.string.tab_notes);
            case 1:
                return Main.getMainActivity().getString(R.string.tab_tags);
        }
        return "";
    }

    @Override
    public int getCount() {
        return 2;
    }

}
