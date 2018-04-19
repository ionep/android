package news.scrip.scripnews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new Menu();
            case 1:
                return new Home();
            case 2:
                return new Extra();
            default:
                return new Home();
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }


}

