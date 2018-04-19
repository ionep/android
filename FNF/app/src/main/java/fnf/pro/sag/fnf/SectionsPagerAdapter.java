package fnf.pro.sag.fnf;

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
                return new Logs();
            case 1:
                return new Favourites();
            case 2:
                return new Contact();
            default:
                return new Logs();
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Logs";
            case 1:
                return "Favourites";
            case 2:
                return "Contact";
            default:
                return "Logs";
        }
    }
}
